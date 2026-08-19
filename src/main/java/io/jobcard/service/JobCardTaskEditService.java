package io.jobcard.service;

import io.activitylog.form.CreateActivityLogForm;
import io.jobcard.entity.JobCardStatus;
import io.jobcard.entity.JobCardTask;
import io.jobcard.form.JobCardTaskCreationForm;
import io.jobcard.form.JobCardTaskEditingForm;
import io.jobcard.repository.JobCardTaskRepository;
import io.lib.form.SessionUserIdForm;
import io.lib.service.BaseJpaRepoEditService;
import io.task.service.TaskReadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class JobCardTaskEditService extends BaseJpaRepoEditService<JobCardTask, JobCardTaskRepository> {

    private JobCardReadService jobCardReadService;
    private JobCardEditService jobCardEditService;
    private JobCardTaskReadService jobCardTaskReadService;
    private TaskReadService taskReadService;

    public List<JobCardTask> createJobCardTask(String jobCardTaskId, JobCardTaskCreationForm form){

        var jobCard = jobCardReadService.findByEntityId(jobCardTaskId);

        List<String> allTaskEntityIds = new ArrayList<>(form.getTasksEntityIds());
        List <String> existingTaskIds = jobCardTaskReadService.findAllJobCardTasksEntityIdsByJobCardId(jobCardTaskId);
        allTaskEntityIds.removeAll(existingTaskIds);
        var distinctEntityIdsThatQualifyForCreation = allTaskEntityIds.stream().distinct().toList();

        List<JobCardTask> jobCardTasks = new ArrayList<>();
        for(String taskEntityId : distinctEntityIdsThatQualifyForCreation){
            var task = taskReadService.findByEntityId(taskEntityId);

            var jobCardTask = new JobCardTask();
            jobCardTask.setJobCard(jobCard);
            jobCardTask.setTask(task);
            jobCardTask.setCreatedByEntityId(form.getSessionUserId());

            jobCardTasks.add(jobCardTask);
        }

        jobCardTasks = save(jobCardTasks, form.getSessionUserId());

        for (JobCardTask savedTask : jobCardTasks) {
            var activityLogForm = new CreateActivityLogForm();
            activityLogForm.setOwningEntityId(savedTask.getEntityId());
            activityLogForm.setAction("Job card task creation");
            activityLogForm.setSessionUserId(form.getSessionUserId());

            activityLogQueuingService.enqueueActivityLog(activityLogForm);
        }

        return jobCardTasks;
    }

    public JobCardTask updateJobCardTaskStatus(String jobCardTaskId, JobCardTaskEditingForm editForm) {
        var jobCardTask = findByEntityId(jobCardTaskId);
        jobCardTask.setStatus(editForm.getStatus());
        save(jobCardTask, editForm.getSessionUserId());

        var jobCardEntityId = jobCardTask.getJobCard().getEntityId();
        updateStatusInTheJobCard(jobCardEntityId, editForm.getSessionUserId());

        var activityLogForm = new CreateActivityLogForm();
        activityLogForm.setOwningEntityId(jobCardTask.getEntityId());
        activityLogForm.setAction("Job card task update");
        activityLogForm.setSessionUserId(editForm.getSessionUserId());

        activityLogQueuingService.enqueueActivityLog(activityLogForm);

        return jobCardTask;
    }

    public void softDeleteJobCardTask(String entityId, SessionUserIdForm deleteForm) {
        var jobCardTask = findByEntityId(entityId);
        delete(jobCardTask, deleteForm.getSessionUserId());
    }

    private void updateStatusInTheJobCard(String jobCardEntityId, String currentUserEntityId) {
        if (jobCardReadService.allJobCardTasksAreDone(jobCardEntityId)) {
            jobCardEditService.updateStatus(
                    jobCardEntityId,
                    JobCardStatus.DONE,
                    currentUserEntityId
            );
        }
    }

    @Autowired
    public void setJobCardReadService(JobCardReadService service) {
        this.jobCardReadService = service;
    }

    @Autowired
    public void setJobCardEditService(JobCardEditService service) {
        this.jobCardEditService = service;
    }

    @Autowired
    public void setJobCardTaskReadService(JobCardTaskReadService service) {
        this.jobCardTaskReadService = service;
    }

    @Autowired
    public void setTaskReadService(TaskReadService service) {
        this.taskReadService = service;
    }

}
