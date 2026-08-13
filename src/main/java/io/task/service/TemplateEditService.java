package io.task.service;

import io.activitylog.form.CreateActivityLogForm;
import io.lib.exception.CommonRuntimeException;
import io.lib.exception.ExceptionType;
import io.lib.form.SessionUserIdForm;
import io.lib.service.BaseJpaRepoEditService;
import io.task.entity.Template;
import io.task.form.TemplateEditForm;
import io.task.form.TemplateRegisterForm;
import io.task.repository.TemplateRepository;
import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
@Service
public class TemplateEditService extends BaseJpaRepoEditService<Template,TemplateRepository> {
    private TemplateTaskEditService templateTaskEditService;

    public Template resigterTemplate(TemplateRegisterForm templateRegisterForm){
        var template = new Template();
        template.setName(templateRegisterForm.getName());
        template.setDescription(templateRegisterForm.getDescription());
        template.setCreatedByEntityId(templateRegisterForm.getSessionUserId());

        template = save(template,templateRegisterForm.getSessionUserId());
        
        var activityLogForm = new CreateActivityLogForm();
        activityLogForm.setOwningEntityId(template.getEntityId());
        activityLogForm.setAction("template creation");
        activityLogForm.setSessionUserId(templateRegisterForm.getSessionUserId());
        activityLogQueuingService.enqueueActivityLog(activityLogForm);

        templateTaskEditService.registerTemplateTask(
            template,
            List.copyOf(templateRegisterForm.getTemplateIds()),
            templateRegisterForm.getSessionUserId()
        );

        return template;
    }

    public Template editTemplate(TemplateEditForm templateEditForm , String templateId){
        var spec = repository.notDeleted()
            .and(repository.nameIs(templateEditForm.getName()))
            .and(repository.entityIdNot(templateId));

        boolean exists = repository.exists(spec);

        if(exists){
            throw new CommonRuntimeException(ExceptionType.BAD_REQUEST, "error.duplicate.name");
        }

        Template template = findByEntityId(templateId);
        template.setName(templateEditForm.getName());
        template.setDescription(templateEditForm.getDescription());
        template.setUpdatedByEntityId(templateEditForm.getSessionUserId());

        template = save(template,templateEditForm.getSessionUserId());
        
        var activityLogForm = new CreateActivityLogForm();
        activityLogForm.setOwningEntityId(template.getEntityId());
        activityLogForm.setAction("template update");
        activityLogForm.setSessionUserId(templateEditForm.getSessionUserId());
        activityLogQueuingService.enqueueActivityLog(activityLogForm);
    
        templateTaskEditService.deleteTemplateTaskByTemplate(template);
        templateTaskEditService.registerTemplateTask(
            template,
            List.copyOf(templateEditForm.getTemplateIds()),
            templateEditForm.getSessionUserId()
        );

        return template;
    }
    public void deleteTemplate(String entityId, SessionUserIdForm sessionUserIdForm) {
        var template = findByEntityId(entityId);
        delete(template, sessionUserIdForm.getSessionUserId());
    }

    @Autowired
    public void setTemplateTaskEditService(TemplateTaskEditService templateTaskEditService){
        this.templateTaskEditService = templateTaskEditService;
    }
}
