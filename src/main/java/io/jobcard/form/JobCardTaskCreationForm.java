package io.jobcard.form;

import io.lib.form.SessionUserIdForm;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class JobCardTaskCreationForm extends SessionUserIdForm {

    @NotEmpty(message = "error.invalid.task.id")
    private List<String> tasksEntityIds = new ArrayList<>();
}

