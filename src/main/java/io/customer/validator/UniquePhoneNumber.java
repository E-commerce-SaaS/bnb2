package io.customer.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = UniquePhoneNumberValidator.class)
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface UniquePhoneNumber {

    String message() default "Phone number is already registered";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
