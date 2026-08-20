package io.jobcard.view;

import io.jobcard.entity.JobCard;
import io.jobcard.entity.JobCardTask;
import io.jobcard.entity.JobCardTaskStatus;
import io.lib.view.BaseView;
import io.task.entity.Task;

public class JobCardTaskView extends BaseView<JobCardTask> {

    public JobCardTaskView(JobCardTask entity) {
        super(entity);
    }

    public JobCard getJobCard(){
        return entity.getJobCard();
    }

    public Task getTask(){
      return entity.getTask();
    }

    public JobCardTaskStatus getStatus(){
        return entity.getStatus();
    }

}
