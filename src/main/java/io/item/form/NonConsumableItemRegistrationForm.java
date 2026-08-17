package io.item.form;

import io.item.validator.UniqueNonConsumableName;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class NonConsumableItemRegistrationForm extends NonConsumableEditForm{

    @UniqueNonConsumableName 
    public String getName(){
        return super.getName();
    }

}
