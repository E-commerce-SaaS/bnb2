package io.jobcardtask.service;

import io.activitylog.form.CreateActivityLogForm;
import io.jobcard.entity.JobCardStatus;
import io.jobcardtask.entity.JobCardTask;
import io.jobcardtask.form.JobCardTaskCreationForm;
import io.jobcardtask.form.JobCardTaskEditingForm;
import io.jobcard.service.JobCardEditService;
import io.jobcard.service.JobCardReadService;
import io.jobcardtask.repository.JobCardTaskRepository;
import io.lib.form.SessionUserIdForm;
import io.lib.service.BaseJpaRepoEditService;
import io.task.service.TaskReadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class JobCardTaskEditService extends BaseJpaRepoEditService<JobCardTask, JobCardTaskRepository> {

    private JobCardReadService jobCardReadService;
    private JobCardEditService jobCardEditService;
    private JobCardTaskReadService jobCardTaskReadService;
    private TaskReadService taskReadService;

    public JobCardTask createJobCardTask(JobCardTaskCreationForm form){

        var jobCard = jobCardReadService.findByEntityId(form.getJobCardEntityId());
        var task = taskReadService.findByEntityId(form.getTaskEntityId());

        var jobCardTask = new JobCardTask();
        jobCardTask.setJobCard(jobCard);
        jobCardTask.setTask(task);
        jobCardTask.setCreatedByEntityId(form.getSessionUserId());

        jobCardTask = save(jobCardTask, form.getSessionUserId());

        var activityLogForm = new CreateActivityLogForm();
        activityLogForm.setOwningEntityId(jobCardTask.getEntityId());
        activityLogForm.setAction("Job card task creation");
        activityLogForm.setSessionUserId(form.getSessionUserId());
        activityLogQueuingService.enqueueActivityLog(activityLogForm);

        return jobCardTask;
    }

    public JobCardTask updateJobCardTask(String jobCardTaskId, JobCardTaskEditingForm editForm) {

        var jobCardEntityId = jobCardTaskReadService.getJobCardEntityIdFromJobCardTask(jobCardTaskId);

        var jobCardTask = findByEntityId(jobCardTaskId);

        jobCardTask.setStatus(editForm.getStatus());

        save(jobCardTask, editForm.getSessionUserId());

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
