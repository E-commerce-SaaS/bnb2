package io.item.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = {UniqueConsumableNameValidator.class})
@Target( { ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface UniqueConsumableName {
    String message() default "error.duplicate.name";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
