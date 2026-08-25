package io.customer.service;

import io.customer.entity.Customer;
import io.customer.repository.CustomerRepository;
import io.lib.form.BaseFetchForm;
import io.lib.service.BaseJpaRepoReadService;
import io.micrometer.common.util.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
public class CustomerReadService extends BaseJpaRepoReadService<Customer, CustomerRepository> {
    public Page<Customer> listCustomers(BaseFetchForm form) {
        var spec = repository.notDeleted();
        if (StringUtils.isNotBlank(form.getQuery())) {
            spec = spec.and(repository.nameLike(form.getQuery()));
        }

        return repository.findAll(
                spec,
                repository.defaultPageable(form)
        );
    }

}
