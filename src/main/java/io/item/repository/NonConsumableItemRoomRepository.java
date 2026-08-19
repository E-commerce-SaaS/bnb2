package io.item.repository;

import io.item.entity.NonConsumableRoomItem;
import io.lib.repository.BaseJpaRepository;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

@Repository
public interface NonConsumableItemRoomRepository extends BaseJpaRepository<NonConsumableRoomItem> {
    default Specification<NonConsumableRoomItem> roomEntityIdIs(String roomEntityId) {
        return (root, query, cb) -> cb.equal(root.get("roomEntityId"), roomEntityId);
    }

    default Specification<NonConsumableRoomItem> nonConsumableItemEntityIdIs(String nonConsumableItemEntityId) {
        return (root, query, cb) -> cb.equal(root.get("nonConsumableItemEntityId"), nonConsumableItemEntityId);
    }
}