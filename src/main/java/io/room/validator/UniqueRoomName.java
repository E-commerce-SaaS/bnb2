package io.room.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = {UniqueRoomNameValidator.class})
@Target( { ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)

public @interface UniqueRoomName {
    String message() default "Name already exists.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
