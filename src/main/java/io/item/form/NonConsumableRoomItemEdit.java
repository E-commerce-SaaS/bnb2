package io.item.form;

import io.lib.form.SessionUserIdForm;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NonConsumableRoomItemEdit extends SessionUserIdForm{

    @NotBlank
    private String nonConsumableItemId;

    @NotNull
    @Min(0)
    private Integer quantity;
}