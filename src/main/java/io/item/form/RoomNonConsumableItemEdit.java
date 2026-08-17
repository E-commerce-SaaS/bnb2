package io.item.form;

import java.util.List;

import io.lib.form.SessionUserIdForm;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoomNonConsumableItemEdit extends SessionUserIdForm {

    @NotBlank
    private String roomId;

    @NotEmpty
   private List<ChildRoomNonConsumableItemEdit> items;
}