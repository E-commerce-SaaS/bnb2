package io.item.service;

import io.lib.form.SessionUserIdForm;
import io.room.service.RoomReadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.activitylog.form.CreateActivityLogForm;
import io.item.entity.NonConsumableRoomItem;
import io.item.form.RoomNonConsumableItemEditForm;
import io.item.repository.NonConsumableItemRoomRepository;
import io.lib.service.BaseJpaRepoEditService;

@Service
public class NonConsumableRoomItemEditService extends BaseJpaRepoEditService<NonConsumableRoomItem, NonConsumableItemRoomRepository> {

    private RoomReadService roomReadService;
    private NonConsumableItemReadService nonConsumableItemReadService;

    public NonConsumableRoomItem addItem(String roomEntityId, RoomNonConsumableItemEditForm form) {

        var spec = repository.notDeleted()
            .and(repository.roomEntityIdIs(roomEntityId))
            .and(repository.nonConsumableItemEntityIdIs(form.getNonConsumableItemId()));

        var roomItem = repository.findOne(spec).orElse(null);

        if(roomItem != null){
            return roomItem;
        }

        var room = roomReadService.findByEntityId(roomEntityId);
        var nonConsumableItem = nonConsumableItemReadService.findByEntityId(form.getNonConsumableItemId());
        roomItem = new NonConsumableRoomItem();
        roomItem.setRoom(room);
        roomItem.setNonConsumableItem(nonConsumableItem);
        roomItem.setQuantity(form.getQuantity());
        roomItem.setCreatedByEntityId(form.getSessionUserId());
        roomItem = save(roomItem, form.getSessionUserId());

        var activityLogForm = new CreateActivityLogForm();
        activityLogForm.setOwningEntityId(roomItem.getEntityId());
        activityLogForm.setAction("Room NonConsumable item added");
        activityLogForm.setSessionUserId(form.getSessionUserId());
        activityLogQueuingService.enqueueActivityLog(activityLogForm);

        return roomItem;
    }

    public void deleteItem(String roomNonConsumableItemEntityId, SessionUserIdForm form) {
        var roomItem = findByEntityId(roomNonConsumableItemEntityId);
        delete(roomItem, form.getSessionUserId());

        var activityLogForm = new CreateActivityLogForm();
        activityLogForm.setOwningEntityId(roomItem.getEntityId());
        activityLogForm.setAction("Room item deleted");
        activityLogForm.setSessionUserId(form.getSessionUserId());

        activityLogQueuingService.enqueueActivityLog(activityLogForm);
    }

    @Autowired
    public void setRoomReadService(RoomReadService service) {
        this.roomReadService = service;
    }

    @Autowired
    public void setNonConsumableItemReadService(NonConsumableItemReadService service) {
        this.nonConsumableItemReadService = service;
    }
}