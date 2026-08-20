package io.jobcard.controller;

import io.jobcard.entity.JobCardStatus;
import io.jobcard.form.JobCardCreationForm;
import io.jobcard.form.JobCardEditingForm;
import io.jobcard.form.JobCardStatusEditingForm;
import io.jobcard.service.JobCardEditService;
import io.jobcard.service.JobCardReadService;
import io.jobcard.view.JobCardView;
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

import java.util.Locale;

import static io.lib.service.SystemConfig.INTERNAL_USER_BASE_URL;

@RestController
@RequestMapping(INTERNAL_USER_BASE_URL + "/job-cards")
public class JobCardController {

    private JobCardEditService jobCardEditService;
    private JobCardReadService jobCardReadService;

    @PreAuthorize("hasAuthority('VIEW_JOBCARDS')")
    @GetMapping("list")
    public PagedEntityApiResponse<JobCardView> list(
            @RequestParam(value = "pageNum", required = false, defaultValue = "0") Integer pageNum,
            @RequestParam(value = "pageSize", required = false, defaultValue = "100") Integer pageSize,
            @RequestParam(value = "query", required = false) String query) {
        var form = new BaseFetchForm();
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

    @PreAuthorize("hasAuthority('UPDATE_JOBCARD')")
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
    @PostMapping("delete/{jobCardId}")
    public void delete(
            @PathVariable String jobCardId,
            Authentication auth) {
        var form = new SessionUserIdForm();
        form.setSessionUserId(auth.getName());
        jobCardEditService.softDeleteJobCard(jobCardId, form);
    }

    @PreAuthorize("hasAuthority('UPDATE_JOBCARD_TO_INPROGRESS')")
    @PostMapping("update-status-to-in-progress/{jobCardId}")
    public EntityApiResponse<JobCardView> markJobCardAsWorkInProgress(
            @RequestBody @Valid JobCardStatusEditingForm form,
            @PathVariable String jobCardId,
            Authentication auth,
            Locale locale) {
        form.setSessionUserId(auth.getName());
        form.setStatus(JobCardStatus.WORK_IN_PROGRESS);
        var jobCard = jobCardEditService.markJobCardAsWorkInProgress(jobCardId, form);
        return new EntityApiResponse<>(
                Message.get("jobcard.status.update.success", locale),
                new JobCardView(jobCard)
        );
    }

    @PreAuthorize("hasAuthority('UPDATE_JOBCARD_TO_DONE')")
    @PostMapping("update-status-done/{jobCardId}")
    public EntityApiResponse<JobCardView> markJobCardAsDone(
            @RequestBody @Valid JobCardStatusEditingForm form,
            @PathVariable String jobCardId,
            Authentication auth,
            Locale locale) {
        form.setSessionUserId(auth.getName());
        form.setStatus(JobCardStatus.DONE);
        var jobCard = jobCardEditService.markJobCardAsDone(jobCardId, form);
        return new EntityApiResponse<>(
                Message.get("jobcard.status.update.success", locale),
                new JobCardView(jobCard)
        );
    }

    @PreAuthorize("hasAuthority('UPDATE_JOBCARD_TO_INSPECTED')")
    @PostMapping("{jobCardId}/update-status-to-inspected")
    public EntityApiResponse<JobCardView> markJobCardAsInspected(
            @RequestBody @Valid JobCardStatusEditingForm form,
            @PathVariable String jobCardId,
            Authentication auth,
            Locale locale) {
        form.setSessionUserId(auth.getName());
        form.setStatus(JobCardStatus.INSPECTED);
        var jobCard = jobCardEditService.markJobCardAsInspected(jobCardId, form);
        return new EntityApiResponse<>(
                Message.get("jobcard.status.update.success", locale),
                new JobCardView(jobCard)
        );
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
