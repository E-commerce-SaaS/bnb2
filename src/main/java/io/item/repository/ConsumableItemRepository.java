package io.item.repository;

import io.item.entity.ConsumableItem;
import io.lib.repository.BaseJpaRepository;
import org.springframework.data.jpa.domain.Specification;

public interface ConsumableItemRepository extends BaseJpaRepository<ConsumableItem> {

    default Specification<ConsumableItem> nameIs(String name) {
        return (root, cb, cq) -> cq.equal(root.get("name"), name);
    }

    default Specification<ConsumableItem> nameContains(String name) {
        return (root, cb, cq) ->cq.like(
                cq.lower(root.get("name")), "%" + name.toLowerCase() + "%"
        );
    }
}


