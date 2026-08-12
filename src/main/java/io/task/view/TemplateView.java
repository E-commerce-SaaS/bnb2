package io.task.view;

import io.lib.view.BaseView;
import io.task.entity.Template;

import java.util.Set;
import java.util.stream.Collectors;

public class TemplateView extends BaseView<Template> {

    public TemplateView(Template entity) {
        super(entity);
    }

    public String getName() {
        return entity.getName();
    }

    public String getDescription() {
        return entity.getDescription();
    }

    public Set<String> getTaskIds() {
        return entity.getTemplateTasks()
                .stream()
                .map(templateTask -> templateTask.getTask().getEntityId())
                .collect(Collectors.toSet());
    }
}