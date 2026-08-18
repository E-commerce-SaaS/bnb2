package io.item.view;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class RoomNonConsumablesView {

    private String roomId;
    private List<Item> items;

    public RoomNonConsumablesView(
            String roomId,
            List<Item> items
    ) {
        this.roomId = roomId;
        this.items = items;
    }

    @Getter
    @Setter
    public static class Item {

        private String nonConsumableId;
        private Integer quantity;

        public Item(
                String nonConsumableId,
                Integer quantity
        ) {
            this.nonConsumableId = nonConsumableId;
            this.quantity = quantity;
        }
    }
}