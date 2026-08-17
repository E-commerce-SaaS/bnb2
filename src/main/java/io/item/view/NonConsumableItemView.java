package io.item.view;

import io.item.entity.NonConsumableItem;
import io.lib.view.BaseView;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NonConsumableItemView extends BaseView<NonConsumableItem> {

    public NonConsumableItemView(NonConsumableItem entity) {super(entity);}

    public String getName(){
        return entity.getName();
    }

    public String getDescription(){
        return entity.getDescription();
    }

}
