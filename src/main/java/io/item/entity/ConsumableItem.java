package io.item.entity;


import io.lib.entity.BaseJpaEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class ConsumableItem extends BaseJpaEntity {

    @Column(unique = true, length = 100)
    private String name;

    private String description;

    @Enumerated(EnumType.STRING)
    private UnitOfMeasure unitOfMeasure;

    private Integer parLevel;
}
