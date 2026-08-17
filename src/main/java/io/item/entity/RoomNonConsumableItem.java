package io.item.entity;

import io.lib.entity.BaseJpaEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(
    uniqueConstraints = {
        @UniqueConstraint(
            columnNames = {
                "room_non_consumables_id",
                "non_consumable_item_id"
            }
        )
    }
)
@Getter
@Setter
public class RoomNonConsumableItem extends BaseJpaEntity {

    @ManyToOne
    private RoomNonConsumables roomNonConsumables;

    @ManyToOne
    private NonConsumableItem nonConsumableItem;

    @Min(0)
    private Integer quantity;
}