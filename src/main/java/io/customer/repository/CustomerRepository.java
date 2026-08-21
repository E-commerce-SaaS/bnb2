package io.customer.repository;

import io.customer.entity.Customer;
import io.lib.repository.BaseJpaRepository;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends BaseJpaRepository<Customer> {

    default Specification<Customer> nameIs(String name){
        return (root, cb, cq) -> cq.equal(root.get("name"), name);
    }

    default Specification<Customer> phoneNumberIs(String phoneNumber){
        return (root, cb, cq) -> cq.equal(root.get("phoneNumber"), phoneNumber);
    }
}
