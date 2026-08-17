package io.item.entity;

import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;
import io.room.entity.Room;

@Setter
@Getter
@Table( uniqueConstraints = { @UniqueConstraint(
            columnNames = {"template_id", "task_id"}
        )
    }
)
public class RoomNonConsumables {
    @ManyToOne
    private Room room;

    @ManyToOne
    private NonConsumableItem nonConsumableItem;
    
    @Min(0)
    private Integer quantity;

}
