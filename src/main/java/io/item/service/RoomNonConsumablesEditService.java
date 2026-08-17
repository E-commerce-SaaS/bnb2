package io.item.service;

import io.item.entity.NonConsumableItem;
import io.item.entity.RoomNonConsumableItem;
import io.item.entity.RoomNonConsumables;
import io.item.form.RoomNonConsumableItemEdit;
import io.item.repository.NonConsumableItemRepository;
import io.item.repository.RoomNonConsumableItemRepository;
import io.lib.exception.CommonRuntimeException;
import io.lib.exception.ExceptionType;
import io.lib.service.BaseJpaRepoEditService;
import io.room.entity.Room;
import io.room.repository.RoomRepository;
import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoomNonConsumablesEditService extends BaseJpaRepoEditService<RoomNonConsumables, RoomNonConsumableItemRepository> {

    private RoomRepository roomRepository;
    private NonConsumableItemRepository nonConsumableItemRepository ;
    
    @Transactional
    public RoomNonConsumables register( RoomNonConsumableItemEdit  form) {
        if (repository.existsByRoomEntityId(form.getRoomId())) {

            throw new CommonRuntimeException(ExceptionType.ALREADY_EXISTS,"room.nonconsumables.already.exists");
        }
        Room room = roomRepository.findByEntityId(form.getRoomId()).orElseThrow(() ->
            new RuntimeException("Room not found")
        );
        
        var roomNonConsumables = new RoomNonConsumables();
        roomNonConsumables.setRoom(room);
        roomNonConsumables.setCreatedByEntityId(
        form.getSessionUserId()
        );
   
        for (var itemForm : form.getItems()) {
            NonConsumableItem nonConsumableItem =
            nonConsumableItemRepository.findByEntityId(itemForm.getNonConsumableItemId())
                .orElseThrow(() ->new RuntimeException( "Non-consumable item not found") );

            var roomItem = new RoomNonConsumableItem();

            roomItem.setRoomNonConsumables( roomNonConsumables );
            roomItem.setNonConsumableItem( nonConsumableItem);
            roomItem.setQuantity( itemForm.getQuantity() );
            roomItem.setCreatedByEntityId( form.getSessionUserId() );

            roomNonConsumables.getItems().add(roomItem);
        }
        return save( roomNonConsumables, form.getSessionUserId());
    }


    @Autowired
    public void setRoomRepository(RoomRepository roomRepository){
        this.roomRepository = roomRepository ;
    }

    @Autowired
    public void setNonConsumableItemRepository(NonConsumableItemRepository nonConsumableItemRepository){
        this.nonConsumableItemRepository = nonConsumableItemRepository ;
    }

}
