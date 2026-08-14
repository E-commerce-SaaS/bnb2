package io.jobcard.form;

import io.jobcard.entity.JobCardStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JobCardEditingForm extends AbstractJobCardForm {

    private JobCardStatus status;

}
