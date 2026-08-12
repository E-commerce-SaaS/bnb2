package io.task.validator;

import io.lib.service.BaseJpaRepoReadService;
import io.task.entity.Template;
import io.task.repository.TemplateRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.WordUtils;
import org.springframework.stereotype.Service;

@Service
public class UniqueTemplateNameValidator extends BaseJpaRepoReadService<Template, TemplateRepository> implements ConstraintValidator<UniqueTemplateName, String>{
    @Override
    public boolean isValid(String name, ConstraintValidatorContext constraintValidatorContext) {
        String sanitizedName = WordUtils.capitalize(StringUtils.trimToEmpty(name));

        var spec = repository.notDeleted()
                .and(repository.nameIs(sanitizedName));

        return !repository.exists(spec);
    }

}
