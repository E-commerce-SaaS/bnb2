package io.room.entity;

import io.lib.entity.BaseJpaEntity;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class RoomCategory  extends BaseJpaEntity {
   private String name;
   private String description;
}