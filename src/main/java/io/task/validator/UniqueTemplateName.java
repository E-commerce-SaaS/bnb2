package io.task.validator;

import java.lang.annotation.*;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;


@Documented
@Constraint(validatedBy = {UniqueTemplateNameValidator.class})
@Target({ElementType.FIELD , ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface UniqueTemplateName {
    String message() default "error.duplicate.name";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

}
