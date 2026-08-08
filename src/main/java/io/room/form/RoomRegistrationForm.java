package io.room.form;

import io.room.validator.UniqueRoomName;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoomRegistrationForm extends RoomEditForm{
    @UniqueRoomName
    public String getName() {
        return super.getName();
    }

    @NotBlank(message = "error.invalid.org.branch")
    private String orgBranchEntityId;

}
