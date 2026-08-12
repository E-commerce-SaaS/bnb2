package io.housekeeping.form;

import io.housekeeping.entity.UnitOfMeasure;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ConsumableItemEditForm {

    @NotBlank
    @Size(max = 100)
    private String name;

    private String description;

    @NotNull
    private UnitOfMeasure unitOfMeasure;

    @NotNull
    private Integer parLevel;

    private String sessionUserId;

}
