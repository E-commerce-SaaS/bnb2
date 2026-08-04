package io.room.service;

import io.activitylog.form.CreateActivityLogForm;
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
    private RoomRepository roomRepository;

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
    public Room updateRoom(RoomEditForm roomEditForm ,String entityId ){
        Room room = roomRepository.findByEntityId(entityId)
            .orElseThrow(() -> new RuntimeException("Room not found"));
        
        room.setName(roomEditForm.getName());
        room.setPricePerNight(roomEditForm.getPricePerNight());
        room.setReservationStatus(roomEditForm.getReservationStatus());

        room = save(room ,roomEditForm.getSessionUserId());
        var activityLogForm = new CreateActivityLogForm();
        activityLogForm.setOwningEntityId(room.getEntityId());
        activityLogForm.setAction("Room update");
        activityLogForm.setSessionUserId(roomEditForm.getSessionUserId());
        activityLogQueuingService.enqueueActivityLog(activityLogForm);

        return room;

    }

    @Autowired
    public void setOrgBranchReadService(OrgBranchReadService service) {
        this.orgBranchReadService = service;
    }

    @Autowired
    public void setRoomRepository(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

}
