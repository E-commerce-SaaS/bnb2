package io.customer.form;

import io.customer.validator.UniqueCustomerPhoneNumber;

public class CustomerRegistrationForm extends CustomerEditForm{

    public String getName() {
        return super.getName();
    }

    @Override
    @UniqueCustomerPhoneNumber
    public String getPhoneNumber() {
        return super.getPhoneNumber();
    }
}
