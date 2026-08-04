package io.room.form;

import io.lib.form.SessionUserIdForm;
import io.room.entity.ReservationStatus;
import jakarta.validation.constraints.NotBlank;
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

    private String createdById;

    private BigDecimal pricePerNight;

    private ReservationStatus reservationStatus;

    public String getName() {
        return WordUtils.capitalize(StringUtils.trimToEmpty(name));
    }
}