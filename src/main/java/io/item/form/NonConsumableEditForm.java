package io.item.form;

import org.apache.commons.lang.WordUtils;

import io.lib.form.SessionUserIdForm;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import org.apache.commons.lang.StringUtils;


@Setter
@Getter
public class NonConsumableEditForm extends SessionUserIdForm{
    @NotBlank(message = "error.empty.name")
    protected String name;
    
    @NotBlank(message = "error.invalid.description")
    private String description;

    public String getName() {
        return WordUtils.capitalize(StringUtils.trimToEmpty(name)).trim();
    }


}
