package io.task.form;

import io.lib.form.BaseFetchForm;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaskFetchForm extends BaseFetchForm {
    private String taskTitle;
    private String taskDescription;
}