package io.room.service;

import io.activitylog.form.CreateActivityLogForm;
import io.lib.exception.CommonRuntimeException;
import io.lib.exception.ExceptionType;
import io.lib.service.BaseJpaRepoEditService;
import io.orgbranch.service.OrgBranchReadService;
import io.room.entity.Room;
import io.room.form.RoomEditForm;
import io.room.form.RoomRegistrationForm;
import io.room.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoomEditService extends BaseJpaRepoEditService<Room, RoomRepository> {
    private OrgBranchReadService orgBranchReadService;

    public Room registerRoom(RoomRegistrationForm form){
        var orgBranch = orgBranchReadService.findByEntityId(form.getOrgBranchEntityId());

        var room = new Room();
        room.setOrgBranch(orgBranch);
        room.setName(form.getName());
        room.setFloor(form.getFloor());
        room.setRoomCategory(form.getRoomCategory());
        room.setPricePerNight(form.getPricePerNight());
        room.setCreatedByEntityId(form.getSessionUserId());

        room = save(room, form.getSessionUserId());

        var activityLogForm = new CreateActivityLogForm();
        activityLogForm.setOwningEntityId(room.getEntityId());
        activityLogForm.setAction("Room creation");
        activityLogForm.setSessionUserId(form.getSessionUserId());
        activityLogQueuingService.enqueueActivityLog(activityLogForm);

        return room;
    }

    public Room updateRoom(String roomId, RoomEditForm editForm){
        checkNameExists(editForm.getName(), roomId);

        var room = findByEntityId(roomId);
        room.setName(editForm.getName());
        room.setPricePerNight(editForm.getPricePerNight());

        room = save(room ,editForm.getSessionUserId());

        var activityLogForm = new CreateActivityLogForm();
        activityLogForm.setOwningEntityId(room.getEntityId());
        activityLogForm.setAction("Room update");
        activityLogForm.setSessionUserId(editForm.getSessionUserId());
        activityLogQueuingService.enqueueActivityLog(activityLogForm);

        return room;
    }

    private void checkNameExists(String roomId, String name){
        var spec = repository.notDeleted()
                .and(repository.nameIs(name))
                .and(repository.entityIdNot(roomId));
        boolean exists = repository.exists(spec);
        if(exists){
            throw new CommonRuntimeException(ExceptionType.BAD_REQUEST, "error.duplicate.name");
        }
    }

    @Autowired
    public void setOrgBranchReadService(OrgBranchReadService service) {
        this.orgBranchReadService = service;
    }
}
