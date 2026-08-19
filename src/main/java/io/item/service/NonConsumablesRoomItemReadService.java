package io.item.service;

import io.item.entity.NonConsumableRoomItem;
import io.item.form.RoomItemFetchForm;
import io.item.repository.NonConsumableItemRoomRepository;
import io.lib.service.BaseJpaRepoReadService;


import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
public class NonConsumablesRoomItemReadService extends BaseJpaRepoReadService<NonConsumableRoomItem, NonConsumableItemRoomRepository> {
    
    public Page<NonConsumableRoomItem> list(RoomItemFetchForm form) {
        var spec = repository.notDeleted()
            .and(repository.roomEntityIdIs(form.getRoomEntityId()));
        return repository.findAll(spec, repository.defaultPageable(form));
    }
    
}