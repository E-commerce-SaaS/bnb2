package io.customer.form;

import io.lib.form.SessionUserIdForm;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.WordUtils;

@Getter
@Setter
public class CustomerEditForm extends SessionUserIdForm {

    @NotBlank(message = "error.invalid.name")
    private String name;

    @NotBlank(message = "error.invalid.phoneNumber")
    private String phoneNumber;

    public String getName() {
        return WordUtils.capitalize(StringUtils.trimToEmpty(name)).trim();
    }

    public String getPhoneNumber() {
        return StringUtils.trimToEmpty(phoneNumber).trim();
    }
}
