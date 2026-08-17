package io.jobcard.service;

import io.activitylog.form.CreateActivityLogForm;
import io.internaluser.service.InternalUserReadService;
import io.jobcard.entity.JobCard;
import io.jobcard.form.JobCardCreationForm;
import io.jobcard.form.JobCardEditingForm;
import io.jobcard.entity.JobCardStatus;
import io.jobcard.form.JobCardStatusEditingForm;
import io.jobcard.repository.JobCardRepository;
import io.lib.exception.CommonRuntimeException;
import io.lib.exception.ExceptionType;
import io.lib.form.SessionUserIdForm;
import io.lib.service.BaseJpaRepoEditService;
import io.room.service.RoomReadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JobCardEditService extends BaseJpaRepoEditService<JobCard, JobCardRepository> {

    private InternalUserReadService internalUserReadService;
    private RoomReadService roomReadService;
    private JobCardReadService jobCardReadService;

    public JobCard createJobCard(JobCardCreationForm form){

        var staff = internalUserReadService.findByEntityId(form.getStaffEntityId());
        var room = roomReadService.findByEntityId(form.getRoomEntityId());

        var jobCard = new JobCard();
        jobCard.setStaff(staff);
        jobCard.setRoom(room);
        jobCard.setCreatedByEntityId(form.getSessionUserId());

        jobCard = save(jobCard, form.getSessionUserId());

        var activityLogForm = new CreateActivityLogForm();
        activityLogForm.setOwningEntityId(jobCard.getEntityId());
        activityLogForm.setAction("Job card creation");
        activityLogForm.setSessionUserId(form.getSessionUserId());
        activityLogQueuingService.enqueueActivityLog(activityLogForm);

        return jobCard;
    }

    public JobCard updateJobCard(String jobCardId, JobCardEditingForm editForm){

        var jobCardExists = jobCardExists(jobCardId, editForm.getStaffEntityId());

        if(jobCardExists){
            throw new CommonRuntimeException(ExceptionType.BAD_REQUEST, "error.duplicate.jobcard");
        }

        var staff = internalUserReadService.findByEntityId(editForm.getStaffEntityId());

        var jobCard = findByEntityId(jobCardId);
        jobCard.setStaff(staff);

        save(jobCard ,editForm.getSessionUserId());

        var activityLogForm = new CreateActivityLogForm();
        activityLogForm.setOwningEntityId(jobCard.getEntityId());
        activityLogForm.setAction("Job card update");
        activityLogForm.setSessionUserId(editForm.getSessionUserId());
        activityLogQueuingService.enqueueActivityLog(activityLogForm);

        return jobCard;
    }

    public void softDeleteJobCard(String entityId, SessionUserIdForm deleteForm) {
        var jobCard = findByEntityId(entityId);
        delete(jobCard, deleteForm.getSessionUserId());
    }

    public JobCard markJobCardAsWorkInProgress(String jobCardId, JobCardStatusEditingForm editForm){

        List<JobCardStatus> inValidStatuses = List.of(JobCardStatus.DONE, JobCardStatus.INSPECTED);

        return(movingTheStatusFromOneLevelToTheNext(jobCardId, editForm, inValidStatuses));
    }

    public JobCard markJobCardAsDone(String jobCardId, JobCardStatusEditingForm editForm){

        List<JobCardStatus> inValidStatuses = List.of(JobCardStatus.INSPECTED);

        return(movingTheStatusFromOneLevelToTheNext(jobCardId, editForm, inValidStatuses));
    }

    public JobCard markJobCardAsInspected(String jobCardId, JobCardStatusEditingForm editForm){


        List<JobCardStatus> inValidStatuses = List.of(JobCardStatus.WORK_IN_PROGRESS);

        return(movingTheStatusFromOneLevelToTheNext(jobCardId, editForm, inValidStatuses));

    }

    private JobCard movingTheStatusFromOneLevelToTheNext(String jobCardId, JobCardStatusEditingForm editForm, List<JobCardStatus> invalidCurrentStatuses){
        var spec = repository.hasEntityId(jobCardId)
                .and(repository.notDeleted());


        JobCard jobCard = repository.findOne(spec)
                .orElseThrow(() -> new CommonRuntimeException(ExceptionType.NOT_FOUND,  "error.invalid.job.card.id"));

        if (jobCard.getStatus() != null && invalidCurrentStatuses.contains(jobCard.getStatus())) {
            throw new CommonRuntimeException(ExceptionType.BAD_REQUEST, "error.invalid.status");
        }

        var  updatedJobCard = updateStatus(jobCardId, editForm.getStatus(), editForm.getSessionUserId());

        var activityLogForm = new CreateActivityLogForm();
        activityLogForm.setOwningEntityId(jobCard.getEntityId());
        activityLogForm.setAction("Job card status update");
        activityLogForm.setSessionUserId(editForm.getSessionUserId());
        activityLogQueuingService.enqueueActivityLog(activityLogForm);

        return updatedJobCard;

    }

    public JobCard updateStatus(String jobCardId, JobCardStatus newStatus, String statusUpdatedByEntityId) {
        var spec = repository.hasEntityId(jobCardId)
                .and(repository.notDeleted());

        JobCard jobCard = repository.findOne(spec)
                .orElseThrow(() -> new CommonRuntimeException(ExceptionType.NOT_FOUND,  "error.invalid.job.card.id"));

        if (newStatus == JobCardStatus.DONE && !jobCardReadService.allJobCardTasksAreDone(jobCardId)) {
            throw new CommonRuntimeException(ExceptionType.FORBIDDEN, "jobcard.status.update.fail");
        }

        jobCard.setStatus(newStatus);
        jobCard = save(jobCard, statusUpdatedByEntityId);

        return jobCard;
    }

    private boolean jobCardExists(String jobCardId, String staffEntityId) {
        var spec = repository.notDeleted()
                .and(repository.staffEntityIdIs(staffEntityId)
                        .and(repository.entityIdNot(jobCardId)));

        return repository.exists(spec);
    }

    @Autowired
    public void setInternalUserReadService(InternalUserReadService service) {

        this.internalUserReadService = service;
    }

    @Autowired
    public void setJobCardReadService(JobCardReadService service) {

        this.jobCardReadService = service;
    }

    @Autowired
    public void setRoomReadService(RoomReadService service) {

        this.roomReadService = service;
    }

}
