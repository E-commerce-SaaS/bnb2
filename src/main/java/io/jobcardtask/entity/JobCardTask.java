package io.jobcardtask.entity;

import io.jobcard.entity.JobCard;
import io.lib.entity.BaseJpaEntity;
import io.task.entity.Task;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;



@Getter
@Setter
@Entity
public class JobCardTask extends BaseJpaEntity {

    @ManyToOne
    private JobCard jobCard;

    @ManyToOne
    private Task task;

    @Enumerated(EnumType.STRING)
    private JobCardTaskStatus status = JobCardTaskStatus.PENDING;

}
