package io.item.form;

import io.lib.form.BaseFetchForm;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class RoomItemFetchForm extends BaseFetchForm {
    private String roomEntityId;
}
