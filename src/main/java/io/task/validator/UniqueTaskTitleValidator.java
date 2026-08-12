package io.task.validator;

import io.lib.service.BaseJpaRepoReadService;
import io.task.entity.Task;
import io.task.repository.TaskRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.WordUtils;
import org.springframework.stereotype.Component;

@Component
class UniqueTaskTitleValidator extends BaseJpaRepoReadService<Task, TaskRepository> implements ConstraintValidator<UniqueTaskTitle, String> {
    @Override
    public boolean isValid(String taskTitle, ConstraintValidatorContext constraintValidatorContext) {
        String sanitizedTaskTitle = WordUtils.capitalize(StringUtils.trimToEmpty(taskTitle));

        var spec = repository.notDeleted()
                .and(repository.taskTitleIs(sanitizedTaskTitle));

        return !repository.exists(spec);
    }
}
