package io.item.validator;

import java.lang.annotation.*;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;


@Documented
@Constraint(validatedBy = {UniqueNonConsumableNameValidator.class})
@Target({ElementType.FIELD , ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface UniqueNonConsumableName {
    String message() default "error.duplicate.name";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

}
