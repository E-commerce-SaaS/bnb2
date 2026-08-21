package io.customer.controller;

import io.customer.form.CustomerRegistrationForm;
import io.customer.service.CustomerEditService;
import io.customer.service.CustomerReadService;
import io.customer.view.CustomerView;
import io.item.service.ConsumableItemEditService;
import io.item.service.ConsumableItemReadService;
import io.lib.service.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.lib.view.EntityApiResponse;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;

import static io.lib.service.SystemConfig.INTERNAL_USER_BASE_URL;


@RestController
@RequestMapping(INTERNAL_USER_BASE_URL + "/customer")
public class CustomerController {
    private CustomerEditService customerEditService;
    private CustomerReadService customerReadService;

    @PreAuthorize("hasAuthority('REGISTER_CUSTOMER')")
    @PostMapping("/register")
    public EntityApiResponse<CustomerView> registerCustomer(
            @RequestBody @Valid CustomerRegistrationForm form,
            Authentication auth,
            Locale locale) {
        form.setSessionUserId(auth.getName());
        var customer = customerEditService.registerCustomer(form);

        return new EntityApiResponse<>(
                Message.get("Customer.registration.success", locale),
                new CustomerView(customer)
        );
    }


    @Autowired
    public void setCustomerEditServiceEditService(CustomerEditService service) {
        this.customerEditService = service;
    }

    @Autowired
    public void setCustomerReadService(CustomerReadService service) {
        this.customerReadService = service;
    }
}
