package io.jobcardtask.form;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JobCardTaskCreationForm extends AbstractJobCardTaskForm {

    @NotBlank(message = "error.invalid.job.card.id")
    private String jobCardEntityId;

    @NotBlank(message = "error.invalid.task.id")
    private String taskEntityId;
}

