package io.task.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = {UniqueTaskTitleValidator.class})
@Target( { ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface UniqueTaskTitle {
    String message() default "error.duplicate.task";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
