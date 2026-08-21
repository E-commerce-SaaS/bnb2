package io.task.view;

import io.lib.view.BaseView;
import io.task.entity.Task;

public class TaskView extends BaseView<Task> {
    public TaskView(Task entity) {
        super(entity);
    }

    public String getName(){
        return entity.getName();
    }

    public String getDescription() { return entity.getDescription(); }

}
