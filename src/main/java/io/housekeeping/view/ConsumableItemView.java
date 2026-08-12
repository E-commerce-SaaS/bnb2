package io.housekeeping.view;

import io.housekeeping.entity.ConsumableItem;
import io.housekeeping.entity.UnitOfMeasure;
import io.lib.view.BaseView;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ConsumableItemView extends BaseView<ConsumableItem> {

    public ConsumableItemView(ConsumableItem entity) {super(entity);}

    public String getName(){return entity.getName();}
    public String getDescription(){return entity.getDescription();}
    public UnitOfMeasure getUnitOfMeasure(){return entity.getUnitOfMeasure();}
    public Integer getParLevel(){return entity.getParLevel();}
}
