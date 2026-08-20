package io.jobcard.form;

import io.jobcard.entity.JobCardTaskStatus;
import io.lib.form.SessionUserIdForm;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JobCardTaskEditingForm extends SessionUserIdForm {

    private JobCardTaskStatus status;

}
