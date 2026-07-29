package io.userauthentication.form;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InitLoginForm extends UsernameTypeForm {

    @NotBlank(message = "error.invalid.password")
    private String password;
}
