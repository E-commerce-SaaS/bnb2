package io.item.service;

import io.item.entity.RoomNonConsumables;
import io.item.repository.RoomNonConsumableItemRepository;
import io.lib.service.BaseJpaRepoReadService;


import org.springframework.stereotype.Service;

@Service
public class RoomNonConsumablesReadService extends BaseJpaRepoReadService<RoomNonConsumables, RoomNonConsumableItemRepository> {
    public RoomNonConsumables findByRoom(String roomEntityId) {
        return repository.findByRoomEntityId(roomEntityId).orElseThrow(() -> new RuntimeException("Room non-consumables not found" )
                );
    }
}