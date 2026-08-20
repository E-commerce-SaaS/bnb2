package io.item.form;

import io.lib.form.SessionUserIdForm;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoomNonConsumableItemEditForm extends SessionUserIdForm{

    @NotBlank(message = "error.invalid.non.consumable.id")
    private String nonConsumableItemId;

    @Min(value = 1, message = "error.invalid.quantity")
    private Integer quantity;
}