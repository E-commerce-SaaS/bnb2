package io.jobcardtask.entity;

import io.jobcard.entity.JobCard;
import io.lib.entity.BaseJpaEntity;
import io.task.entity.Task;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(
        indexes = {@Index(name = "idx_job_card_entity_id_index", columnList = "jobCardEntityId")},
        uniqueConstraints = {
                @UniqueConstraint(name = "unique_constraint_job_card_task", columnNames = {"job_card_id", "task_id"})
        }
)
public class JobCardTask extends BaseJpaEntity {

    @ManyToOne
    @JoinColumn(name = "job_card_id")
    private JobCard jobCard;

    private String jobCardEntityId;

    @ManyToOne
    @JoinColumn(name = "task_id")
    private Task task;

    @Enumerated(EnumType.STRING)
    private JobCardTaskStatus status = JobCardTaskStatus.PENDING;


    public void setJobCard(JobCard jobCard) {
        this.jobCard = jobCard;
        if(this.jobCard != null){
            this.jobCardEntityId = jobCard.getEntityId();
        }
    }
}
