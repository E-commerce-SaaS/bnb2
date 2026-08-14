package io.jobcard.service;

import io.activitylog.form.CreateActivityLogForm;
import io.internaluser.service.InternalUserReadService;
import io.jobcard.entity.JobCard;
import io.jobcard.form.JobCardCreationForm;
import io.jobcard.form.JobCardEditingForm;
import io.jobcard.entity.JobCardStatus;
import io.jobcard.repository.JobCardRepository;
import io.lib.exception.CommonRuntimeException;
import io.lib.exception.ExceptionType;
import io.lib.exception.ResourceNotFoundException;
import io.lib.form.SessionUserIdForm;
import io.lib.service.BaseJpaRepoEditService;
import io.room.service.RoomReadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

        if(editForm.getStatus().equals(JobCardStatus.DONE) || editForm.getStatus().equals(JobCardStatus.INSPECTED)){
            if(!jobCardReadService.allJobCardTasksAreDone(jobCardId)){
                throw new CommonRuntimeException(ExceptionType.BAD_REQUEST, "error.incomplete.jobcard.tasks");
            }
        }

        var staff = internalUserReadService.findByEntityId(editForm.getStaffEntityId());

        var jobCard = findByEntityId(jobCardId);
        jobCard.setStaff(staff);
        jobCard.setStatus(editForm.getStatus());

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

    public void updateStatus(String jobCardId, JobCardStatus newStatus) {
        var spec = repository.hasId(jobCardId)
                .and(repository.notDeleted());

        JobCard jobCard = repository.findOne(spec)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("JobCard with ID '%s' was not found or has been deleted.", jobCardId)));

        if (newStatus == JobCardStatus.DONE && !jobCardReadService.allJobCardTasksAreDone(jobCardId)) {
            throw new IllegalStateException("Cannot change status to DONE because associated tasks are incomplete.");
        }

        jobCard.setStatus(newStatus);
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
