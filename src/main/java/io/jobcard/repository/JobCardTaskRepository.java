package io.jobcard.repository;

import io.jobcard.entity.JobCardTask;
import io.lib.repository.BaseJpaRepository;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;


@Repository
public interface JobCardTaskRepository extends BaseJpaRepository<JobCardTask>{

    default Specification<JobCardTask> jobCardEntityIdIs(String jobCardId) {
        return (root, query, cb) -> cb.equal(root.get("jobCardEntityId"), jobCardId);
    }
}
