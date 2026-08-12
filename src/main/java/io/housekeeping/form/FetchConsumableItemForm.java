package io.housekeeping.form;

import io.lib.form.BaseFetchForm;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FetchConsumableItemForm extends BaseFetchForm {
    private String query;

}
