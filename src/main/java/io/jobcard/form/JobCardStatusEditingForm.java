package io.jobcard.form;

import io.jobcard.entity.JobCardStatus;
import io.lib.form.SessionUserIdForm;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JobCardStatusEditingForm extends SessionUserIdForm {

    private JobCardStatus status;

}
