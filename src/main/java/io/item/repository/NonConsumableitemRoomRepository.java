package io.item.repository;

import java.util.List;
import io.item.entity.NonConsumableRoomItem;
import io.lib.repository.BaseJpaRepository;

public interface NonConsumableitemRoomRepository extends BaseJpaRepository<NonConsumableRoomItem> {

        boolean existsByRoomEntityIdAndNonConsumableItemEntityId(String roomId,String nonConsumableItemId);
                
        boolean existsByRoomEntityId(String roomId);

        List<NonConsumableRoomItem> findByRoomEntityId(String roomId);
    

}