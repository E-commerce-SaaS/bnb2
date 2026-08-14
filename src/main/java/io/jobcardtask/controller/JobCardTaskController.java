package io.jobcardtask.controller;

import io.jobcardtask.form.JobCardTaskCreationForm;
import io.jobcardtask.form.JobCardTaskEditingForm;
import io.jobcardtask.form.JobCardTaskFetchForm;
import io.jobcardtask.service.JobCardTaskEditService;
import io.jobcardtask.service.JobCardTaskReadService;
import io.jobcardtask.view.JobCardTaskView;
import io.lib.form.SessionUserIdForm;
import io.lib.service.Message;
import io.lib.view.EntityApiResponse;
import io.lib.view.PagedEntityApiResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;

import static io.lib.service.SystemConfig.INTERNAL_USER_BASE_URL;


@RestController
@RequestMapping(INTERNAL_USER_BASE_URL + "/jobcardtasks")
public class JobCardTaskController {

    private JobCardTaskEditService jobCardTaskEditService;
    private JobCardTaskReadService jobCardTaskReadService;

    @PreAuthorize("hasAuthority('VIEW_JOBCARDTASKS')")
    @GetMapping("list/{jobcardId}")
    public PagedEntityApiResponse<JobCardTaskView> list(
            @PathVariable String jobcardId,
            @RequestParam(value = "pageNum", required = false, defaultValue = "0") Integer pageNum,
            @RequestParam(value = "pageSize", required = false, defaultValue = "100") Integer pageSize) {

        var form = new JobCardTaskFetchForm();
        form.setQuery(jobcardId);
        form.setPageNum(pageNum);
        form.setPageSize(pageSize);

        var page = jobCardTaskReadService.listJobCardTasks(form);

        var views = page.stream()
                .map(JobCardTaskView::new)
                .toList();

        return new PagedEntityApiResponse<>(page, views);
    }


    @PreAuthorize("hasAuthority('CREATE_JOBCARDTASK')")
    @PostMapping("create")
    public EntityApiResponse<JobCardTaskView> registerTask(
            @RequestBody @Valid JobCardTaskCreationForm form,
            Authentication auth,
            Locale locale) {
        form.setSessionUserId(auth.getName());
        var jobCardTask = jobCardTaskEditService.createJobCardTask(form);

        return new EntityApiResponse<>(
                Message.get("jobcardtask.registration.success", locale),
                new JobCardTaskView(jobCardTask)
        );
    }

    @PreAuthorize("hasAuthority('EDIT_JOBCARDTASK')")
    @PostMapping("update/{jobcardtaskId}")
    public EntityApiResponse<JobCardTaskView> update(
            @RequestBody @Valid JobCardTaskEditingForm form,
            @PathVariable String jobcardtaskId,
            Authentication auth,
            Locale locale) {
        form.setSessionUserId(auth.getName());
        var jobCardTask = jobCardTaskEditService.updateJobCardTask(jobcardtaskId, form);
        return new EntityApiResponse<>(
                Message.get("jobcardtask.edit.success", locale),
                new JobCardTaskView(jobCardTask)
        );
    }

    @PreAuthorize("hasAuthority('DELETE_JOBCARDTASK')")
    @PostMapping("delete/{entityId}")
    public void delete(
            @PathVariable String entityId,
            Authentication auth) {
        var form = new SessionUserIdForm();
        form.setSessionUserId(auth.getName());
        jobCardTaskEditService.softDeleteJobCardTask(entityId, form);
    }

    @Autowired
    public void setJobCardTaskEditService(JobCardTaskEditService service) {
        this.jobCardTaskEditService = service;
    }


    @Autowired
    public void setJobCardTaskReadService(JobCardTaskReadService service) {
        this.jobCardTaskReadService = service;
    }

}