package io.task.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import io.lib.service.BaseJpaRepoEditService;
import io.task.entity.Task;
import io.task.entity.Template;
import io.task.entity.TemplateTask;
import io.task.repository.TemplateTaskRepository;
import java.util.ArrayList;


@Service
public class TemplateTaskEditService extends BaseJpaRepoEditService<TemplateTask, TemplateTaskRepository>{

    private TaskReadService taskReadService;
 
    void registerTemplateTask(Template template ,List<String> taskIds,String createdById){
        List<Task> tasks= taskReadService.findByIds(taskIds);
        List<TemplateTask> templateTasks = new ArrayList<>();

        for(Task task : tasks){
            TemplateTask templateTask = new TemplateTask();

            templateTask.setTemplate(template);
            templateTask.setTask(task);
            templateTask.setCreatedByEntityId(createdById);

            templateTasks.add(templateTask);
        }
        save(templateTasks, createdById);
    }

    void deleteTemplateTaskByTemplate(Template template){
        Specification<TemplateTask> spec = repository.notDeleted()
        .and(repository.templateIdIn(List.of(template)));
        repository.deleteAll(repository.findAll(spec));

    }


    @Autowired
    public void setTaskservice(TaskReadService taskReadService){
        this.taskReadService = taskReadService;
    }

}
