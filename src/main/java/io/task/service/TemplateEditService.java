package io.task.service;

import io.activitylog.form.CreateActivityLogForm;
import io.lib.service.BaseJpaRepoEditService;
import io.task.entity.Template;
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

        templateTaskEditService.registerTemplateTask(template,List.copyOf(templateRegisterForm.getTemplateIds()), templateRegisterForm.getSessionUserId());

        return template;
    }

    @Autowired
    public void setTemplateTaskEditService(TemplateTaskEditService templateTaskEditService){
        this.templateTaskEditService = templateTaskEditService;
    }
}
