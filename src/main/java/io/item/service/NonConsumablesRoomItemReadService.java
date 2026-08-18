package io.item.service;

import io.item.entity.NonConsumableRoomItem;
import io.item.repository.NonConsumableitemRoomRepository;
import io.lib.service.BaseJpaRepoReadService;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class NonConsumablesRoomItemReadService extends BaseJpaRepoReadService<NonConsumableRoomItem, NonConsumableitemRoomRepository> {
    
    public List<NonConsumableRoomItem> findByRoomId(String roomId) {
        return repository.findByRoomEntityId(roomId);
    }
    
}