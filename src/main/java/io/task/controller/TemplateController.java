package io.task.controller;

import java.util.Locale;

import org.springframework.security.core.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.task.service.TemplateEditService;
import io.task.view.TemplateView;
import jakarta.validation.Valid;
import io.task.form.TemplateRegisterForm;
import io.lib.service.Message;
import io.lib.view.EntityApiResponse;


import static io.lib.service.SystemConfig.INTERNAL_USER_BASE_URL;

@RestController
@RequestMapping(INTERNAL_USER_BASE_URL + "/templates")
public class TemplateController {
    private TemplateEditService templateEditService;

    @PreAuthorize("hasAuthority('REGISTER_TASK_TEMPLATE')")
    @PostMapping("register")
    public EntityApiResponse<TemplateView> register(
            @RequestBody @Valid TemplateRegisterForm form,
            Authentication auth,
            Locale locale) {
        form.setSessionUserId(auth.getName());
        var template = templateEditService.resigterTemplate(form);
        return new EntityApiResponse<>(
            Message.get("template.registration.success", locale),
            new TemplateView(template)
        );
    }

    @Autowired
    public void setTemplateEditService(TemplateEditService service) {
        this.templateEditService = service;
    }
}
