package io.item.form;


import io.item.validator.UniqueConsumableName;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ConsumableItemRegistrationForm extends ConsumableItemEditForm {

    @UniqueConsumableName
    public String getName() {
        return super.getName();
    }
}
