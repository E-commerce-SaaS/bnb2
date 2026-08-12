package io.task.form;

import io.task.validator.UniqueTaskTitle;

public class TaskRegistrationForm extends TaskEditForm{

    @UniqueTaskTitle
    public String getTaskTitle(){
        return super.getTaskTitle();
    }
}
