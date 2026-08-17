package io.item.repository;

import io.item.entity.NonConsumableItem;
import io.lib.repository.BaseJpaRepository;
import org.springframework.data.jpa.domain.Specification;

public interface NonConsumableItemRepository extends BaseJpaRepository<NonConsumableItem> {

    default Specification<NonConsumableItem> nameIs(String name) {
        return (root, cb, cq) -> cq.equal(root.get("name"), name);
    }

    default Specification<NonConsumableItem> nameContains(String name) {
        return (root, cb, cq) ->cq.like(
                cq.lower(root.get("name")), "%" + name.toLowerCase() + "%"
        );
    }
}


