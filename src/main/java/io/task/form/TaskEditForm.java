package io.task.form;

import io.lib.form.SessionUserIdForm;
import io.task.validator.UniqueTaskTitle;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaskEditForm extends SessionUserIdForm {
    @NotBlank(message = "error.invalid.task.title")
    @UniqueTaskTitle
    private String taskTitle;

    private String taskDescription;
}
