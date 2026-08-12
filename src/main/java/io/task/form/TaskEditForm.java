package io.task.form;

import io.lib.form.SessionUserIdForm;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaskEditForm extends SessionUserIdForm {
    private String taskTitle;
    private String taskDescription;
}
