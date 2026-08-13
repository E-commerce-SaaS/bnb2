package io.task.repository;

import org.springframework.data.jpa.domain.Specification;

import io.lib.repository.BaseJpaRepository;

import io.task.entity.Template;

public interface TemplateRepository extends BaseJpaRepository<Template>{
    default Specification<Template> nameIs(String name){
        return (root, cb, cq) -> cq.equal(root.get("name"), name);
    }
    

}
