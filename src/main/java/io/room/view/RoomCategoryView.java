package io.room.view;

import io.lib.view.BaseView;
import io.room.entity.RoomCategory;

public class RoomCategoryView extends BaseView<RoomCategory> {

    public RoomCategoryView(RoomCategory entity) {
        super(entity);
    }

    public String getName() {
        return entity.getName();
    }

    public String getDescription(){
        return entity.getDescription();
    }
}
