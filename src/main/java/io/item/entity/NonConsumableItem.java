package io.item.entity;

import io.lib.entity.BaseJpaEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class NonConsumableItem extends BaseJpaEntity {

    @Column(unique = true, length = 100)
    private String name;

    private String description;
}
