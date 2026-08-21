package io.customer.service;

import io.activitylog.form.CreateActivityLogForm;
import io.customer.entity.Customer;
import io.customer.form.CustomerEditForm;
import io.customer.form.CustomerRegistrationForm;
import io.customer.repository.CustomerRepository;
import io.lib.exception.CommonRuntimeException;
import io.lib.exception.ExceptionType;
import io.lib.service.BaseJpaRepoEditService;

import org.springframework.stereotype.Service;

@Service
public class CustomerEditService extends BaseJpaRepoEditService<Customer, CustomerRepository> {

    public Customer registerCustomer(CustomerRegistrationForm form) {
        var customer = new Customer();

        customer.setName(form.getName());
        customer.setPhoneNumber(form.getPhoneNumber());
        customer.setCreatedByEntityId(form.getSessionUserId());

        customer = save(customer, form.getSessionUserId());

        var activityLogForm = new CreateActivityLogForm();
        activityLogForm.setOwningEntityId(customer.getEntityId());
        activityLogForm.setAction("Customer registration");
        activityLogForm.setSessionUserId(form.getSessionUserId());
        activityLogQueuingService.enqueueActivityLog(activityLogForm);

        return customer;
    }

    public Customer editCustomer(String customerId, CustomerEditForm form) {
        var customer = findByEntityId(customerId);

        customer.setName(form.getName());
        customer.setPhoneNumber(form.getPhoneNumber());

        customer = save(customer ,form.getSessionUserId());

        var activityLogForm = new CreateActivityLogForm();
        activityLogForm.setOwningEntityId(customer.getEntityId());
        activityLogForm.setAction("Customer update");
        activityLogForm.setSessionUserId(form.getSessionUserId());
        activityLogQueuingService.enqueueActivityLog(activityLogForm);

        return customer;
    }

    private void checkNameExists(String customerId, String name){
        var spec = repository.notDeleted()
                .and(repository.nameLike(name))
                .and(repository.entityIdNot(customerId));

        boolean exists = repository.exists(spec);

        if(exists){
            throw new CommonRuntimeException(ExceptionType.BAD_REQUEST, "error.duplicate.name");
        }
    }
}
