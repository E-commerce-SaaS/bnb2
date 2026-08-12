package io.task.entity;

import java.util.HashSet;
import java.util.Set;

import io.lib.entity.BaseJpaEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
public class Template extends BaseJpaEntity{

    @Column(unique = true, length = 100)
    String name;
    String description;
    
    @OneToMany(mappedBy = "template")
    private Set<TemplateTask> templateTasks = new HashSet<>();

}
