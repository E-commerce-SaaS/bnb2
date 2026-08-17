package io.item.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.item.entity.RoomNonConsumables;
import io.lib.view.BaseView;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoomNonConsumablesView extends BaseView<RoomNonConsumables> {

    public RoomNonConsumablesView(RoomNonConsumables entity) {
        super(entity);
    }

    public String getRoomId() {
        return entity.getRoom().getEntityId();
    }

    public List<Map<String, Object>> getItems() {
        List<Map<String, Object>> items = new ArrayList<>();

        for (var item : entity.getItems()) {
            Map<String, Object> itemData = new HashMap<>();
            itemData.put("nonConsumableItemId", item.getNonConsumableItem().getEntityId());
            itemData.put("quantity", item.getQuantity());
            items.add(itemData);
        }

        return items;
    }
}