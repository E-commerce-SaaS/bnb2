package io.task.controller;

import io.lib.form.SessionUserIdForm;
import io.lib.service.Message;
import io.lib.view.EntityApiResponse;
import io.lib.view.PagedEntityApiResponse;
import io.task.form.TaskEditForm;
import io.task.form.TaskFetchForm;
import io.task.form.TaskRegistrationForm;
import io.task.service.TaskEditService;
import io.task.service.TaskReadService;
import io.task.view.TaskView;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;

import static io.lib.service.SystemConfig.INTERNAL_USER_BASE_URL;

@RestController
@RequestMapping(INTERNAL_USER_BASE_URL + "/tasks")
public class TaskController {
    private TaskEditService taskEditService;
    private TaskReadService taskReadService;

    @PreAuthorize("hasAuthority('VIEW_TASK')")
    @GetMapping("list")
    public PagedEntityApiResponse<TaskView> list(
            @RequestParam(value = "pageNum", required = false, defaultValue = "0") Integer pageNum,
            @RequestParam(value = "pageSize", required = false, defaultValue = "100") Integer pageSize,
            @RequestParam(value = "query", required = false) String query) {
        var form = new TaskFetchForm();
        form.setQuery(query);
        form.setPageNum(pageNum);
        form.setPageSize(pageSize);
        var page = taskReadService.listTasks(form);
        var views = page.stream().map(TaskView::new).toList();
        return new PagedEntityApiResponse<>(page, views);
    }

    @PreAuthorize("hasAuthority('REGISTER_TASK')")
    @PostMapping("register")
    public EntityApiResponse<TaskView> registerTask(
            @RequestBody @Valid TaskRegistrationForm form,
            Authentication auth,
            Locale locale) {
        form.setSessionUserId(auth.getName());
        var task = taskEditService.registerTask(form);

        return new EntityApiResponse<>(
                Message.get("task.registration.success", locale),
                new TaskView(task)
        );
    }

    @PreAuthorize("hasAuthority('EDIT_TASK')")
    @PostMapping("update/{taskId}")
    public EntityApiResponse<TaskView> update(
            @RequestBody @Valid TaskEditForm form,
            @PathVariable String taskId,
            Authentication auth,
            Locale locale) {
        form.setSessionUserId(auth.getName());
        var task = taskEditService.updateTask(taskId, form);
        return new EntityApiResponse<>(
                Message.get("task.edit.success", locale),
                new TaskView(task)
        );
    }

    @PreAuthorize("hasAuthority('DELETE_TASK')")
    @PostMapping("delete/{entityId}")
    public void delete(
            @PathVariable String entityId,
            Authentication auth) {
        var form = new SessionUserIdForm();
        form.setSessionUserId(auth.getName());
        taskEditService.softDeleteTask(entityId, form);
    }

    @Autowired
    public void setTaskEditService(TaskEditService service) {
        this.taskEditService = service;
    }


    @Autowired
    public void setTaskReadService(TaskReadService service) {
        this.taskReadService = service;
    }

}
