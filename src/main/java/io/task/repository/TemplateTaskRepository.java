package io.task.repository;

import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import io.lib.repository.BaseJpaRepository;
import io.task.entity.Template;
import io.task.entity.TemplateTask;

public interface TemplateTaskRepository extends BaseJpaRepository<TemplateTask> {
    default Specification<TemplateTask> templateIdIn(List<Template> templates){
        return (root ,query,builder) ->root.get("template").in(templates);
    }

}