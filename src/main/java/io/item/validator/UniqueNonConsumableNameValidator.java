package io.item.validator;

import io.item.entity.NonConsumableItem;
import io.item.repository.NonConsumableItemRepository;
import io.lib.service.BaseJpaRepoReadService;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.WordUtils;
import org.springframework.stereotype.Service;

@Service
public class UniqueNonConsumableNameValidator extends BaseJpaRepoReadService<NonConsumableItem, NonConsumableItemRepository>
 implements ConstraintValidator<UniqueNonConsumableName, String>{
    @Override
    public boolean isValid(String name, ConstraintValidatorContext constraintValidatorContext) {
        String sanitizedName = WordUtils.capitalize(StringUtils.trimToEmpty(name));

        var spec = repository.notDeleted()
                .and(repository.nameIs(sanitizedName));

        return !repository.exists(spec);
    }

}
