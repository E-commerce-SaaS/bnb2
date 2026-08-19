package io.room.form;

import io.lib.form.BaseFetchForm;
import io.room.entity.ReservationStatus;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class FetchRoomForm extends BaseFetchForm {
    private String roomCategoryId;
    private ReservationStatus reservationStatus;
    private String orgBranchId;
}
