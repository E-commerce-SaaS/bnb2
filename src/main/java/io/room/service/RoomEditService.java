package io.room.service;

import io.activitylog.form.CreateActivityLogForm;
import io.lib.service.BaseJpaRepoEditService;
import io.room.entity.Room;
import io.room.form.RoomRegistrationForm;
import io.room.repository.RoomRepository;
import org.springframework.stereotype.Service;

@Service
public class RoomEditService extends BaseJpaRepoEditService<Room, RoomRepository> {
    public Room registerRoom(RoomRegistrationForm form){
        var room = new Room();
        room.setName(form.getName());
        room.setCreatedByEntityId(form.getSessionUserId());
        room.setOrgBranch(form.getOrgBranch());
        room.setPricePerNight(form.getPricePerNight());
        room.setReservationStatus(form.getReservationStatus());
        room.setFloor(form.getFloor());
        room.setRoomCategory(form.getRoomCategory());
        save(room, form.getSessionUserId());

        var activityLogForm = new CreateActivityLogForm();
        activityLogForm.setOwningEntityId(room.getEntityId());
        activityLogForm.setAction("Room creation");
        activityLogForm.setSessionUserId(form.getSessionUserId());
        activityLogQueuingService.enqueueActivityLog(activityLogForm);

        return room;
    }
}
