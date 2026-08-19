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
    private RoomCategoryReadService roomCategoryReadService;

    public Room registerRoom(RoomRegistrationForm form){
        var orgBranch = orgBranchReadService.findByEntityId(form.getOrgBranchEntityId());
        var roomCategory = roomCategoryReadService.findByEntityId(form.getRoomCategoryEntityId());

        var room = new Room();
        room.setOrgBranch(orgBranch);
        room.setName(form.getName());
        room.setFloorNumber(form.getFloorNumber());
        room.setRoomCategory(roomCategory);
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

    public Room editRoom(String roomId, RoomEditForm form){
        checkNameExists(roomId, form.getName());

        var roomCategory = roomCategoryReadService.findByEntityId(form.getRoomCategoryEntityId());
        var orgBranch = orgBranchReadService.findByEntityId(form.getOrgBranchEntityId());

        var room = findByEntityId(roomId);
        room.setName(form.getName());
        room.setFloorNumber(form.getFloorNumber());
        room.setRoomCategory(roomCategory);
        room.setOrgBranch(orgBranch);
        room.setPricePerNight(form.getPricePerNight());

        room = save(room ,form.getSessionUserId());

        var activityLogForm = new CreateActivityLogForm();
        activityLogForm.setOwningEntityId(room.getEntityId());
        activityLogForm.setAction("Room update");
        activityLogForm.setSessionUserId(form.getSessionUserId());
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

    @Autowired
    public void setRoomCategoryReadService(RoomCategoryReadService service) {
        this.roomCategoryReadService = service;
    }
}
