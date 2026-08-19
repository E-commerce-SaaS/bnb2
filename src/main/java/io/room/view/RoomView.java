package io.room.view;

import io.lib.view.BaseView;
import io.room.entity.ReservationStatus;
import io.room.entity.Room;
import io.room.entity.RoomCategory;

import java.math.BigDecimal;

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

    public BigDecimal getPricePerNight(){
        return entity.getPricePerNight();
    }

    public String getFloor(){
        return entity.getFloor();
    }

    public ReservationStatus  getReservationStatus(){
        return entity.getReservationStatus();
    }
}
