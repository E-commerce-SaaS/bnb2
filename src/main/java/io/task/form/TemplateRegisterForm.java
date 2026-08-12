package io.task.form;

import io.task.validator.UniqueTemplateName;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TemplateRegisterForm extends TemplateEditForm{
    @UniqueTemplateName
    public String getName(){
        return super.getName();
    }

}
