package io.item.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

import io.lib.entity.BaseJpaEntity;
import io.room.entity.Room;

@Setter
@Getter
@Entity
@Table(
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"room_id"})
    }
)
public class RoomNonConsumables extends BaseJpaEntity {

    @OneToOne
    private Room room;

    @OneToMany(
        mappedBy = "roomNonConsumables",
        cascade = CascadeType.ALL,
        orphanRemoval = true
    )
    private List<RoomNonConsumableItem> items = new ArrayList<>();
}