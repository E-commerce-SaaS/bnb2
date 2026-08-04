package io.room.form;

import io.orgbranch.entity.OrgBranch;
import io.room.entity.RoomCategory;
import io.room.validator.UniqueRoomName;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoomRegistrationForm extends RoomEditForm{
    @UniqueRoomName(message = "error.duplicate.name")
    public String getName() {
        return super.getName();
    }

    private OrgBranch orgBranch;

    private String floor;

    private RoomCategory roomCategory;
}
