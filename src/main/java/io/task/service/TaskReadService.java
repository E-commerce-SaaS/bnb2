package io.task.service;

import io.lib.service.BaseJpaRepoReadService;
import io.task.entity.Task;
import io.task.form.TaskFetchForm;
import io.task.repository.TaskRepository;
import org.apache.commons.lang.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class TaskReadService extends BaseJpaRepoReadService<Task, TaskRepository> {

    public Page<Task> listTasks(TaskFetchForm form){
        return repository.findAll(createSpecification(form),repository.defaultPageable(form));
    }

    private Specification<Task> createSpecification(TaskFetchForm form){
        var spec = repository.notDeleted();

        if(StringUtils.isNotBlank(form.getQuery())){
            spec = spec.and(repository.taskTitleLike(form.getQuery()));
        }
        return spec;
    }
}