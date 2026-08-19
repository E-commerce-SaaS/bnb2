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

    public String getRoomCategoryName(){
        return entity.getRoomCategory() == null
            ? null
            : entity.getRoomCategory().getName();
    }

    public String getOrgBranchName(){
        return entity.getOrgBranch() == null
            ? null
            : entity.getOrgBranch().getName();
    }

    public BigDecimal getPricePerNight(){
        return entity.getPricePerNight();
    }

    public String getFloorNumber(){
        return entity.getFloorNumber();
    }

    public ReservationStatus  getReservationStatus(){
        return entity.getReservationStatus();
    }
}
