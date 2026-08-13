package io.task.entity;

import io.lib.entity.BaseJpaEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table( uniqueConstraints = { @UniqueConstraint(
            columnNames = {"template_id", "task_id"}
        )
    }
)
public class TemplateTask extends BaseJpaEntity{
    @ManyToOne
    private Task task;

    @ManyToOne
    private Template template;

}
