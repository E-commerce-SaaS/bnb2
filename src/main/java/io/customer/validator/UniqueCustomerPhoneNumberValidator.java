package io.customer.validator;

import io.customer.entity.Customer;
import io.customer.repository.CustomerRepository;
import io.lib.service.BaseJpaRepoReadService;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;

@Component
public class UniqueCustomerPhoneNumberValidator extends BaseJpaRepoReadService<Customer, CustomerRepository>
        implements ConstraintValidator<UniqueCustomerPhoneNumber, String> {

    @Override
    public boolean isValid(String phoneNumber, ConstraintValidatorContext context) {
        if (phoneNumber == null || phoneNumber.isBlank()) {
            return true;
        }

        var spec = repository.notDeleted()
                .and(repository.phoneNumberIs(phoneNumber));

        return !repository.exists(spec);
    }
}
