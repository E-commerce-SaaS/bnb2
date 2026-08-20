package io.item.entity;

import io.lib.entity.BaseJpaEntity;
import io.room.entity.Room;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(
    indexes = {
        @Index(name = "idx_room_entity_id", columnList = "roomEntityId"),
        @Index(name = "idx_non_consumable_item_entity_id" , columnList = "nonConsumableItemEntityId")
    }
)
@Getter
@Setter
public class NonConsumableRoomItem extends BaseJpaEntity {

    @ManyToOne
    private Room room;
    private String roomEntityId;

    @ManyToOne
    private NonConsumableItem nonConsumableItem;
    private String nonConsumableItemEntityId;

    private Integer quantity;

    public void setRoom(Room room) {
        this.room = room;
        if(this.room != null){
            this.roomEntityId = room.getEntityId();
        }
    }

    public void setNonConsumableItem(NonConsumableItem nonConsumableItem) {
        this.nonConsumableItem = nonConsumableItem;
        if(this.nonConsumableItem != null){
            this.nonConsumableItemEntityId = nonConsumableItem.getEntityId();
        }
    }
}