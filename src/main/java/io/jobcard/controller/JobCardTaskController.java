package io.jobcard.controller;

import io.jobcard.form.JobCardTaskCreationForm;
import io.jobcard.form.JobCardTaskEditingForm;
import io.jobcard.service.JobCardTaskEditService;
import io.jobcard.service.JobCardTaskReadService;
import io.jobcard.view.JobCardTaskView;
import io.lib.form.BaseFetchForm;
import io.lib.form.SessionUserIdForm;
import io.lib.service.Message;
import io.lib.view.EntityApiResponse;
import io.lib.view.PagedEntityApiResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Locale;

import static io.lib.service.SystemConfig.INTERNAL_USER_BASE_URL;


@RestController
@RequestMapping(INTERNAL_USER_BASE_URL + "/job-card-tasks")
public class JobCardTaskController {

    private JobCardTaskEditService jobCardTaskEditService;
    private JobCardTaskReadService jobCardTaskReadService;

    @PreAuthorize("hasAuthority('VIEW_JOBCARD')")
    @GetMapping("list/{jobCardId}")
    public PagedEntityApiResponse<JobCardTaskView> list(
            @PathVariable String jobCardId,
            @RequestParam(value = "pageNum", required = false, defaultValue = "0") Integer pageNum,
            @RequestParam(value = "pageSize", required = false, defaultValue = "100") Integer pageSize) {

        var form = new BaseFetchForm();
        form.setQuery(jobCardId);
        form.setPageNum(pageNum);
        form.setPageSize(pageSize);

        var page = jobCardTaskReadService.listJobCardTasks(form);

        var views = page.stream()
                .map(JobCardTaskView::new)
                .toList();

        return new PagedEntityApiResponse<>(page, views);
    }

    @PreAuthorize("hasAuthority('CREATE_JOBCARD')")
    @PostMapping("add-tasks/{jobCardId}")
    public EntityApiResponse<List<JobCardTaskView>> addTasks(
            @RequestBody @Valid JobCardTaskCreationForm form,
            @PathVariable String jobCardId,
            Authentication auth,
            Locale locale) {

        form.setSessionUserId(auth.getName());
        var jobCardTasks = jobCardTaskEditService.addTasks(jobCardId, form);

        List<JobCardTaskView> taskViews = jobCardTasks.stream()
                .map(JobCardTaskView::new)
                .toList();

        return new EntityApiResponse<>(
                Message.get("jobcardtask.registration.success", locale),
                taskViews
        );
    }

    @PreAuthorize("hasAuthority('UPDATE_JOBCARD_TASK_STATUS')")
    @PostMapping("update-status/{jobCardTaskId}")
    public EntityApiResponse<JobCardTaskView> update(
            @RequestBody @Valid JobCardTaskEditingForm form,
            @PathVariable String jobCardTaskId,
            Authentication auth,
            Locale locale) {
        form.setSessionUserId(auth.getName());
        var jobCardTask = jobCardTaskEditService.updateJobCardTaskStatus(jobCardTaskId, form);
        return new EntityApiResponse<>(
            Message.get("jobcardtask.edit.success", locale),
            new JobCardTaskView(jobCardTask)
        );
    }

    @PostMapping("delete/{jobCardTaskId}")
    public void delete(
            @PathVariable String jobCardTaskId,
            Authentication auth) {
        var form = new SessionUserIdForm();
        form.setSessionUserId(auth.getName());
        jobCardTaskEditService.softDeleteJobCardTask(jobCardTaskId, form);
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