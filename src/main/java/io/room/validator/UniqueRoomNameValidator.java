package io.room.validator;

import io.lib.service.BaseJpaRepoReadService;
import io.room.entity.Room;
import io.room.repository.RoomRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.WordUtils;
import org.springframework.stereotype.Service;

@Service
public class UniqueRoomNameValidator extends BaseJpaRepoReadService<Room, RoomRepository> implements ConstraintValidator<UniqueRoomName, String>{
    @Override
    public boolean isValid(String name, ConstraintValidatorContext constraintValidatorContext) {
        String sanitizedName = WordUtils.capitalize(StringUtils.trimToEmpty(name));

        var spec = repository.notDeleted()
                .and(repository.nameIs(sanitizedName));

        return !repository.exists(spec);
    }

}
