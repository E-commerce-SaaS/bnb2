package io.task.controller;

import java.util.Locale;

import org.springframework.security.core.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.task.service.TemplateEditService;
import io.task.service.TemplateReadService;

import io.task.view.TemplateView;
import jakarta.validation.Valid;
import io.task.form.TemplateRegisterForm;
import io.lib.form.BaseFetchForm;
import io.lib.service.Message;
import io.lib.view.EntityApiResponse;
import io.lib.view.PagedEntityApiResponse;

import static io.lib.service.SystemConfig.INTERNAL_USER_BASE_URL;

@RestController
@RequestMapping(INTERNAL_USER_BASE_URL + "/templates")
public class TemplateController {
    private TemplateEditService templateEditService;
    private TemplateReadService templateReadService;

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

    @PreAuthorize("hasAuthority('View_TEMPLATE_BY_ID')")
    @GetMapping("{templateId}")
    public EntityApiResponse<TemplateView> veiwSingleTemplate(
        @PathVariable String templateId,
        Locale locale ){
        var template = templateReadService.FindTemplateById(templateId)
        .orElseThrow(() -> new RuntimeException("Template not found"));
        return new EntityApiResponse<>(
            Message.get("template.list.success",locale),
            new TemplateView(template)
        );

    }

    @PreAuthorize("hasAuthority('VIEW_TEMPLATES')")
    @GetMapping("list")
    public PagedEntityApiResponse<TemplateView> list(
            @RequestParam(value = "pageNum", required = false, defaultValue = "0") Integer pageNum,
            @RequestParam(value = "pageSize", required = false, defaultValue = "100") Integer pageSize,
            @RequestParam(value = "query", required = false) String query) {
        var form = new BaseFetchForm();
        form.setQuery(query);
        form.setPageNum(pageNum);
        form.setPageSize(pageSize);
        var page = templateReadService.ListTemplates(form);
        var views = page.stream().map(TemplateView::new).toList();
        return new PagedEntityApiResponse<>(page, views);
    }


    @Autowired
    public void setTemplateEditService(TemplateEditService service) {
        this.templateEditService = service;
    }

    @Autowired
    public void setTemplateReadService(TemplateReadService templateReadService){
        this.templateReadService = templateReadService;
    }
}