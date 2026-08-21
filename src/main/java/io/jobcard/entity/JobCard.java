package io.jobcard.entity;

import io.internaluser.entity.InternalUser;
import io.lib.entity.BaseJpaEntity;
import io.room.entity.Room;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class JobCard extends BaseJpaEntity {

    @ManyToOne
    private InternalUser staff;

    @ManyToOne
    private Room room;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private JobCardStatus status = JobCardStatus.WORK_IN_PROGRESS;

}
