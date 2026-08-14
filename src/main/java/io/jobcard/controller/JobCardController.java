package io.jobcard.controller;

import io.jobcard.form.JobCardCreationForm;
import io.jobcard.form.JobCardEditingForm;
import io.jobcard.form.JobCardFetchForm;
import io.jobcard.service.JobCardEditService;
import io.jobcard.service.JobCardReadService;
import io.jobcard.view.JobCardView;
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
@RequestMapping(INTERNAL_USER_BASE_URL + "/jobcards")
public class JobCardController {

    private JobCardEditService jobCardEditService;
    private JobCardReadService jobCardReadService;

    @PreAuthorize("hasAuthority('VIEW_JOBCARDS')")
    @GetMapping("list")
    public PagedEntityApiResponse<JobCardView> list(
            @RequestParam(value = "pageNum", required = false, defaultValue = "0") Integer pageNum,
            @RequestParam(value = "pageSize", required = false, defaultValue = "100") Integer pageSize,
            @RequestParam(value = "query", required = false) String query) {
        var form = new JobCardFetchForm();
        form.setQuery(query);
        form.setPageNum(pageNum);
        form.setPageSize(pageSize);
        var page = jobCardReadService.listJobCards(form);
        var views = page.stream().map(JobCardView::new).toList();
        return new PagedEntityApiResponse<>(page, views);
    }

    @PreAuthorize("hasAuthority('CREATE_JOBCARD')")
    @PostMapping("create")
    public EntityApiResponse<JobCardView> registerTask(
            @RequestBody @Valid JobCardCreationForm form,
            Authentication auth,
            Locale locale) {
        form.setSessionUserId(auth.getName());
        var jobCard = jobCardEditService.createJobCard(form);

        return new EntityApiResponse<>(
                Message.get("jobcard.registration.success", locale),
                new JobCardView(jobCard)
        );
    }

    @PreAuthorize("hasAuthority('EDIT_JOBCARD')")
    @PostMapping("update/{jobCardId}")
    public EntityApiResponse<JobCardView> update(
            @RequestBody @Valid JobCardEditingForm form,
            @PathVariable String jobCardId,
            Authentication auth,
            Locale locale) {
        form.setSessionUserId(auth.getName());
        var jobCard = jobCardEditService.updateJobCard(jobCardId, form);
        return new EntityApiResponse<>(
                Message.get("jobcard.edit.success", locale),
                new JobCardView(jobCard)
        );
    }

    @PreAuthorize("hasAuthority('DELETE_JOBCARD')")
    @PostMapping("delete/{entityId}")
    public void delete(
            @PathVariable String entityId,
            Authentication auth) {
        var form = new SessionUserIdForm();
        form.setSessionUserId(auth.getName());
        jobCardEditService.softDeleteJobCard(entityId, form);
    }

    @Autowired
    public void setJobCardEditService(JobCardEditService service) {
        this.jobCardEditService = service;
    }


    @Autowired
    public void setJobCardReadService(JobCardReadService service) {
        this.jobCardReadService = service;
    }

}
