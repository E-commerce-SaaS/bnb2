package io.room.view;

import io.lib.view.BaseView;
import io.room.entity.Room;
import io.room.entity.RoomCategory;

public class RoomView extends BaseView<Room> {
    public RoomView(Room entity) {
        super(entity);
    }

    public String getName(){
        return entity.getName();
    }

    public RoomCategory getRoomCategory(){
        return entity.getRoomCategory();
    }
}
