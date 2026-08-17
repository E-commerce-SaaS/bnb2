package io.item.repository;

import java.util.Optional;

import io.item.entity.RoomNonConsumables;
import io.lib.repository.BaseJpaRepository;

public interface RoomNonConsumableItemRepository extends BaseJpaRepository<RoomNonConsumables> {

    boolean existsByRoomEntityId(String roomId);

    Optional<RoomNonConsumables> findByRoomEntityId(String roomEntityId);
}