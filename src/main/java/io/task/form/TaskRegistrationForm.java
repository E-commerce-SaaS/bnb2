package io.task.form;

import io.task.validator.UniqueTaskName;

public class TaskRegistrationForm extends TaskEditForm{

    @UniqueTaskName
    public String getName(){
        return super.getName();
    }
}
