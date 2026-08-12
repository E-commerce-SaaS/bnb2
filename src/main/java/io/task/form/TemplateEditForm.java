package io.task.form;

import java.util.Set;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.WordUtils;

import io.lib.form.SessionUserIdForm;


@Getter
@Setter
public class TemplateEditForm extends SessionUserIdForm{
    @NotBlank(message = "error.empty.name")
    protected String name;
    
    @NotBlank(message = "error.invalid.description")
    private String description;

    @NotEmpty(message = "error.empty.tasks")
    private Set<String> templateIds;


    public String getName() {
        return WordUtils.capitalize(StringUtils.trimToEmpty(name)).trim();
    }

    

}
