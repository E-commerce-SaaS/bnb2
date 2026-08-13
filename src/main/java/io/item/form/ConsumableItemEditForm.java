package io.item.form;

import io.item.entity.UnitOfMeasure;
import io.lib.form.SessionUserIdForm;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.WordUtils;

@Getter
@Setter
public class ConsumableItemEditForm extends SessionUserIdForm {

    @NotBlank(message = "error.invalid.name")
    @Size(max = 100, message = "error.invalid.name.size")
    private String name;

    private String description;

    @NotNull(message = "error.invalid.unit.of.measure")
    private UnitOfMeasure unitOfMeasure;

    @NotNull(message = "error.invalid.par.level")
    private Integer parLevel;

    public String getName(){
        return WordUtils.capitalize(StringUtils.trimToEmpty(name));
    }
}
