package io.jobcard.entity;

import io.internaluser.entity.InternalUser;
import io.lib.entity.BaseJpaEntity;
import io.room.entity.Room;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ManyToOne;
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
    private JobCardStatus status = JobCardStatus.WORK_IN_PROGRESS;

}
