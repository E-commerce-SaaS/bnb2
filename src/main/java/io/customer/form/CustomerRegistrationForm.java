package io.customer.form;

import io.customer.validator.UniquePhoneNumber;

public class CustomerRegistrationForm extends CustomerEditForm{

    public String getName() {
        return super.getName();
    }

    @Override
    @UniquePhoneNumber
    public String getPhoneNumber() {
        return super.getPhoneNumber();
    }
}
