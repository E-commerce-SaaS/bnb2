package io.item.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.activitylog.form.CreateActivityLogForm;
import io.item.entity.NonConsumableRoomItem;
import io.item.form.NonConsumableRoomItemEdit;
import io.item.repository.NonConsumableitemRoomRepository;
import io.item.repository.NonConsumableItemRepository;
import io.lib.exception.CommonRuntimeException;
import io.lib.exception.ExceptionType;
import io.lib.service.BaseJpaRepoEditService;
import io.room.repository.RoomRepository;

@Service
public class NonConsumableRoomItemEditService extends BaseJpaRepoEditService<NonConsumableRoomItem,NonConsumableitemRoomRepository> {

    private RoomRepository roomRepository;
    private NonConsumableItemRepository nonConsumableItemRepository;

    public NonConsumableRoomItem addItem(String roomEntityId,NonConsumableRoomItemEdit form) {

        var room = roomRepository.findByEntityId(roomEntityId)
                .orElseThrow(() -> new CommonRuntimeException(ExceptionType.ALREADY_EXISTS,"room.not.found"
                ));

        if (repository.existsByRoomEntityIdAndNonConsumableItemEntityId( roomEntityId, form.getNonConsumableItemId())) {

            throw new CommonRuntimeException( ExceptionType.NOT_FOUND, "room.nonconsumable.item.already.exists"
            );
        }

        var nonConsumableItem =nonConsumableItemRepository.findByEntityId(form.getNonConsumableItemId())
            .orElseThrow(() -> new CommonRuntimeException(ExceptionType.NOT_FOUND,"nonconsumable.item.not.found"
         ));

        var roomItem = new NonConsumableRoomItem();

        roomItem.setRoom(room);
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

    if (!roomItem.getRoom().getEntityId().equals(roomEntityId)) {
        throw new CommonRuntimeException(
            ExceptionType.NOT_FOUND,
            "room.nonconsumable.item.not.found"
        );
    }

    delete(roomItem, sessionUserId);

    var activityLogForm = new CreateActivityLogForm();
    activityLogForm.setOwningEntityId(roomItem.getEntityId());
    activityLogForm.setAction("Room NonConsumable item deleted");
    activityLogForm.setSessionUserId(sessionUserId);

    activityLogQueuingService.enqueueActivityLog(activityLogForm);
}

    @Autowired
    public void setRoomRepository(RoomRepository repository) {
        this.roomRepository = repository;
    }

    @Autowired
    public void setNonConsumableItemRepository(NonConsumableItemRepository repository) {
        this.nonConsumableItemRepository = repository;
    }
}