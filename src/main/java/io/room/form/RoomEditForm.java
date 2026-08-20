package io.room.form;

import io.lib.form.SessionUserIdForm;
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

    private String floorNumber;

    @NotNull(message = "error.invalid.room.category")
    private String roomCategoryEntityId;

    @NotBlank(message = "error.invalid.org.branch")
    private String orgBranchEntityId;

    private BigDecimal pricePerNight;

    public String getName() {
        return WordUtils.capitalize(StringUtils.trimToEmpty(name)).trim();
    }

    public String getFloorNumber() {
        return WordUtils.capitalize(StringUtils.trimToEmpty(floorNumber)).trim();
    }
}