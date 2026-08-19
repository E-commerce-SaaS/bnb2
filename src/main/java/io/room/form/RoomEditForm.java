package io.room.form;

import io.lib.form.SessionUserIdForm;
import io.room.entity.RoomCategory;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.WordUtils;

import java.math.BigDecimal;

@Getter
@Setter
public class RoomEditForm extends SessionUserIdForm {
    @NotBlank(message = "error.invalid.name")
    private String name;

    private String floor;

    @NotNull(message = "error.invalid.room.category")
    private RoomCategory roomCategory;

    private BigDecimal pricePerNight;

    public String getName() {
        return WordUtils.capitalize(StringUtils.trimToEmpty(name)).trim();
    }

    public String getFloor() {
        return WordUtils.capitalize(StringUtils.trimToEmpty(floor)).trim();
    }
}