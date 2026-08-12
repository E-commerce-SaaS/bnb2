package io.task.form;

import io.lib.form.BaseFetchForm;
import io.task.entity.Task;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaskFetchForm extends BaseFetchForm {
    private Task taskTitle;
    private Task taskDescription;
    private String taskEntityId;
}