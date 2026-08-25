package io.customer.controller;

import io.customer.form.CustomerEditForm;
import io.customer.form.CustomerRegistrationForm;
import io.customer.service.CustomerEditService;
import io.customer.service.CustomerReadService;
import io.customer.view.CustomerView;
import io.lib.form.BaseFetchForm;
import io.lib.service.Message;
import io.lib.view.PagedEntityApiResponse;
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
@RequestMapping(INTERNAL_USER_BASE_URL + "/customers")
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

    @PreAuthorize("hasAuthority('EDIT_CUSTOMER')")
    @PutMapping("edit/{customerId}")
    public EntityApiResponse<CustomerView> edit(
            @PathVariable String customerId,
            @RequestBody @Valid CustomerEditForm form,
            Authentication auth,
            Locale locale) {
        form.setSessionUserId(auth.getName());

        var customer = customerEditService.editCustomer(
                customerId,
                form
        );
        return new EntityApiResponse<>(
                Message.get("customer.edit.success", locale),
                new CustomerView(customer)
        );
    }

    @PreAuthorize("hasAuthority('VIEW_CUSTOMER')")
    @GetMapping("fetch/{customerId}")
    public EntityApiResponse<CustomerView> fetch(
            @PathVariable String customerId) {
        var customer = customerReadService.findByEntityId(customerId);

        return new EntityApiResponse<>(
                new CustomerView(customer)
        );
    }

    @PreAuthorize("hasAuthority('VIEW_CUSTOMER')")
    @GetMapping("list")
    public PagedEntityApiResponse<CustomerView> list(
            @RequestParam(name="query",required = false)String query,
            @RequestParam(name="pageNum",required = false)Integer pageNum,
            @RequestParam(name="pageSize",required = false)Integer pageSize,
            Authentication auth
    ){
        var form = new BaseFetchForm();
        form.setQuery(query);
        form.setPageNum(pageNum);
        form.setPageSize(pageSize);
        form.setSessionUserId(auth.getName());

        var page = customerReadService.listCustomers(form);
        var views = page.getContent()
                .stream()
                .map(CustomerView::new)
                .toList();
        return new PagedEntityApiResponse<>(page, views);
    }


    @Autowired
    public void setCustomerEditService(CustomerEditService service) {
        this.customerEditService = service;
    }

    @Autowired
    public void setCustomerReadService(CustomerReadService service) {
        this.customerReadService = service;
    }
}
