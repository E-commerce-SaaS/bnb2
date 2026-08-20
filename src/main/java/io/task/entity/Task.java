package io.task.entity;

import io.lib.entity.BaseJpaEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Task extends BaseJpaEntity {

    @Column(unique = true, nullable = false, length = 150)
    private String name;

    @Column(length = 500)
    private String description;
}
