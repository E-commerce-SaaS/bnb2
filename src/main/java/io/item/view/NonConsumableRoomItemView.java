package io.item.view;

import io.item.entity.NonConsumableRoomItem;
import io.lib.view.BaseView;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class NonConsumableRoomItemView extends BaseView<NonConsumableRoomItem> {
    public NonConsumableRoomItemView(NonConsumableRoomItem entity) {
        super(entity);
    }

    public String getName(){
        return entity.getNonConsumableItem().getName();
    }

    public Integer getQuantity(){
        return entity.getQuantity();
    }
}