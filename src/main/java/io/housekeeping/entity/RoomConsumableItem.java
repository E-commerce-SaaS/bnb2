package io.housekeeping.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.lib.entity.BaseJpaEntity;
import io.room.entity.Room;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(uniqueConstraints = { @UniqueConstraint(columnNames = { "room_id", "consumable_item_id"})})
public class RoomConsumableItem extends BaseJpaEntity {

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "room_id", nullable = false)
    private Room room;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "consumable_item_id", nullable = false)
    private ConsumableItem consumableItem;

    private Integer quantity;
}
