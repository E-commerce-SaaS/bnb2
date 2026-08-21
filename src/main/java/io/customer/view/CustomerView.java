package io.customer.view;


import io.customer.entity.Customer;
import io.lib.view.BaseView;

public class CustomerView extends BaseView<Customer> {

    public CustomerView(Customer entity) {
        super(entity);
    }

    public String getName() {
        return entity.getName();
    }

    public String getPhoneNumber() {
        return entity.getPhoneNumber();
    }
}
