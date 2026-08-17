package io.item.repository;

import io.item.entity.RoomNonConsumableItem;
import io.lib.repository.BaseJpaRepository;

public interface ChildRoomNonConsumableitemRepository
        extends BaseJpaRepository<RoomNonConsumableItem> {
                boolean existsByRoomNonConsumablesRoomEntityIdAndNonConsumableItemEntityId(
            String roomEntityId,
            String nonConsumableItemEntityId
    );

}