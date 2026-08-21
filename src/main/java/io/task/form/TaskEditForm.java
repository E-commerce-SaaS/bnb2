package io.task.form;

import io.lib.form.SessionUserIdForm;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaskEditForm extends SessionUserIdForm {
    @NotBlank(message = "error.invalid.task.title")
    private String name;

    private String description;
}
