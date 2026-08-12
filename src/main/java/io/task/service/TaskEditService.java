package io.task.service;

import io.activitylog.form.CreateActivityLogForm;
import io.lib.exception.CommonRuntimeException;
import io.lib.exception.ExceptionType;
import io.lib.form.SessionUserIdForm;
import io.lib.service.BaseJpaRepoEditService;
import io.task.entity.Task;
import io.task.form.TaskEditForm;
import io.task.repository.TaskRepository;
import org.springframework.stereotype.Service;

@Service
public class TaskEditService extends BaseJpaRepoEditService<Task, TaskRepository> {

    public Task registerTask(TaskEditForm form){

        var task = new Task();
        task.setTaskTitle(form.getTaskTitle());
        task.setTaskDescription(form.getTaskDescription());
        task.setCreatedByEntityId(form.getSessionUserId());

        task = save(task, form.getSessionUserId());

        var activityLogForm = new CreateActivityLogForm();
        activityLogForm.setOwningEntityId(task.getEntityId());
        activityLogForm.setAction("Task creation");
        activityLogForm.setSessionUserId(form.getSessionUserId());
        activityLogQueuingService.enqueueActivityLog(activityLogForm);

        return task;
    }

    public Task updateTask(String taskId, TaskEditForm editForm){

        if(taskExists(taskId, editForm.getTaskTitle())){
            throw new CommonRuntimeException(ExceptionType.BAD_REQUEST, "error.duplicate.task");
        }

        var task = findByEntityId(taskId);
        task.setTaskTitle(editForm.getTaskTitle());
        task.setTaskDescription(editForm.getTaskDescription());

        save(task ,editForm.getSessionUserId());

        var activityLogForm = new CreateActivityLogForm();
        activityLogForm.setOwningEntityId(task.getEntityId());
        activityLogForm.setAction("Task update");
        activityLogForm.setSessionUserId(editForm.getSessionUserId());
        activityLogQueuingService.enqueueActivityLog(activityLogForm);

        return task;
    }

    public void softDeleteTask(String entityId, SessionUserIdForm deleteForm) {
        var task = findByEntityId(entityId);
        delete(task, deleteForm.getSessionUserId());
    }

    private boolean taskExists(String taskId, String taskTitle){
        var spec = repository.notDeleted()
            .and(repository.taskTitleIs(taskTitle)
            .and(repository.entityIdNot(taskId)));

        return repository.exists(spec);
    }
}