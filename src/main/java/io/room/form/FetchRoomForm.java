package io.room.form;

import io.lib.form.BaseFetchForm;
import io.room.entity.ReservationStatus;
import io.room.entity.RoomCategory;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class FetchRoomForm extends BaseFetchForm {
    private String query;
    private RoomCategory roomCategory;
    private ReservationStatus reservationStatus;
    private String branchEntityId;
    private Integer pageNum;
    private Integer pageSize;
}
