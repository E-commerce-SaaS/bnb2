package io.jobcard.form;


import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JobCardCreationForm extends AbstractJobCardForm {

    @NotBlank(message = "error.invalid.room.id")
    private String roomEntityId;

}
