package io.item.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.activitylog.form.CreateActivityLogForm;
import io.item.entity.RoomNonConsumableItem;
import io.item.form.ChildRoomNonConsumableItemEdit;
import io.item.repository.ChildRoomNonConsumableitemRepository;
import io.item.repository.NonConsumableItemRepository;
import io.item.repository.RoomNonConsumableItemRepository;
import io.lib.exception.CommonRuntimeException;
import io.lib.exception.ExceptionType;
import io.lib.service.BaseJpaRepoEditService;

@Service
public class ChildRoomNonConsumableItemEditService extends BaseJpaRepoEditService<RoomNonConsumableItem,ChildRoomNonConsumableitemRepository> {

    private RoomNonConsumableItemRepository roomNonConsumablesRepository;
    private NonConsumableItemRepository nonConsumableItemRepository;

    public RoomNonConsumableItem addItem(String roomEntityId,ChildRoomNonConsumableItemEdit form) {
        var roomNonConsumables = roomNonConsumablesRepository.findByRoomEntityId(roomEntityId)
                .orElseThrow(() -> new RuntimeException("Room non-consumables not found"));

        if (repository.existsByRoomNonConsumablesRoomEntityIdAndNonConsumableItemEntityId(
                roomEntityId,
                form.getNonConsumableItemId())) {

            throw new CommonRuntimeException(
                    ExceptionType.ALREADY_EXISTS,
                    "room.nonconsumable.item.already.exists"
            );
        }

        var nonConsumableItem = nonConsumableItemRepository.findByEntityId(form.getNonConsumableItemId())
                .orElseThrow(() -> new RuntimeException("Non-consumable item not found"));

        var roomItem = new RoomNonConsumableItem();

        roomItem.setRoomNonConsumables(roomNonConsumables);
        roomItem.setNonConsumableItem(nonConsumableItem);
        roomItem.setQuantity(form.getQuantity());
        roomItem.setCreatedByEntityId(form.getSessionUserId());
        roomItem = save(roomItem,form.getSessionUserId());

        var activityLogForm = new CreateActivityLogForm();
        activityLogForm.setOwningEntityId(roomItem.getEntityId());
        activityLogForm.setAction("Room NonConsumable item added");
        activityLogForm.setSessionUserId(form.getSessionUserId());
        activityLogQueuingService.enqueueActivityLog(activityLogForm);

        return roomItem;
    }

    public void deleteItem(String roomEntityId,String roomNonConsumableItemEntityId,String sessionUserId) {

        var roomItem = findByEntityId(roomNonConsumableItemEntityId);

        if (!roomItem.getRoomNonConsumables().getRoom().getEntityId().equals(roomEntityId)) {
            throw new CommonRuntimeException(
                    ExceptionType.NOT_FOUND,
                    "room.nonconsumable.item.not.found"
            );
        }

        delete(roomItem,sessionUserId);

        var activityLogForm = new CreateActivityLogForm();
        activityLogForm.setOwningEntityId(roomItem.getEntityId());
        activityLogForm.setAction("Room NonConsumable item deleted");
        activityLogForm.setSessionUserId(sessionUserId);
        activityLogQueuingService.enqueueActivityLog(activityLogForm);
    }

    @Autowired
    public void setRoomNonConsumablesRepository(RoomNonConsumableItemRepository repository) {
        this.roomNonConsumablesRepository = repository;
    }

    @Autowired
    public void setNonConsumableItemRepository(NonConsumableItemRepository repository) {
        this.nonConsumableItemRepository = repository;
    }
}