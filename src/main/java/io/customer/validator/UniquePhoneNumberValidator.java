package io.customer.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class UniquePhoneNumberValidator implements ConstraintValidator<UniquePhoneNumber, String> {

    private static final String PHONE_NUMBER_PATTERN = "^\\+?[0-9]{10,15}$";

    @Override
    public boolean isValid(String phoneNumber, ConstraintValidatorContext context) {
        if (phoneNumber == null || phoneNumber.isBlank()) {
            return true;
        }

        return phoneNumber.matches(PHONE_NUMBER_PATTERN);
    }
}
