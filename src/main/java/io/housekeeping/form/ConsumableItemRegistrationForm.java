package io.housekeeping.form;


import io.housekeeping.entity.UnitOfMeasure;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ConsumableItemRegistrationForm{

    @NotBlank(message = "error.invalid.name")
    private String name;

    private String description;

    @NotNull(message = "error.invalid.unit.of.measure")
    private UnitOfMeasure unitOfMeasure;

    @NotNull(message = "error.invalid.par.level")
    private Integer parLevel;

    private String sessionUserId;

}
