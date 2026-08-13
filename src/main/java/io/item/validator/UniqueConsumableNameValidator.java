package io.item.validator;

import io.item.entity.ConsumableItem;
import io.item.repository.ConsumableItemRepository;
import io.lib.service.BaseJpaRepoReadService;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.WordUtils;
import org.springframework.stereotype.Component;

@Component
class UniqueConsumableNameValidator extends BaseJpaRepoReadService<ConsumableItem, ConsumableItemRepository>
        implements ConstraintValidator<UniqueConsumableName, String> {
    @Override
    public boolean isValid(String name, ConstraintValidatorContext constraintValidatorContext) {
        String sanitizedName = WordUtils.capitalize(StringUtils.trimToEmpty(name));

        var spec = repository.notDeleted()
                .and(repository.nameIs(sanitizedName));

        return !repository.exists(spec);
    }
}
