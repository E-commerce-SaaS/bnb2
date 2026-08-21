package io.customer.form;

import io.lib.form.BaseFetchForm;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FetchCustomerForm extends BaseFetchForm {
    private String name;
    private String phoneNumber;
}
