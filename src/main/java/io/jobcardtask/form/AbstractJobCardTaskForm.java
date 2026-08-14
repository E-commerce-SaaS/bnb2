package io.jobcardtask.form;

import io.jobcardtask.entity.JobCardTaskStatus;
import io.lib.form.SessionUserIdForm;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AbstractJobCardTaskForm extends SessionUserIdForm {

    private JobCardTaskStatus status;

}
