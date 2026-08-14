package io.jobcard.form;

import io.lib.form.SessionUserIdForm;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AbstractJobCardForm extends SessionUserIdForm {

    @NotBlank(message = "error.invalid.staff.id")
    private String staffEntityId;

}
