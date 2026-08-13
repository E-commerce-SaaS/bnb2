package io.task.service;

import io.lib.form.BaseFetchForm;
import io.lib.service.BaseJpaRepoReadService;
import io.task.entity.Template;
import io.task.repository.TemplateRepository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class TemplateReadService extends BaseJpaRepoReadService<Template, TemplateRepository> {

     public Page<Template>ListTemplates(BaseFetchForm baseFetchForm){
        var spec = repository.notDeleted();
        if (!baseFetchForm.getQuery().isBlank()){
            spec = spec.and(repository.nameLike(baseFetchForm.getQuery())
            .or(repository.descriptionLike(baseFetchForm.getQuery())));
        }

        Pageable pageable = repository.defaultPageable(baseFetchForm);
        return repository.findAll(spec,pageable);
    }

    public Optional<Template> FindTemplateById (String templateId){
       
        return repository.findByEntityId(templateId);
    }

}