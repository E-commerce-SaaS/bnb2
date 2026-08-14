package io.jobcardtask.repository;

import io.jobcardtask.entity.JobCardTask;
import io.lib.repository.BaseJpaRepository;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;


@Repository
public interface JobCardTaskRepository extends BaseJpaRepository<JobCardTask> , JpaSpecificationExecutor<JobCardTask> {

    default Specification<JobCardTask> jobCardEntityIdIs(String jobCardId) {
        return (root, query, cb) -> cb.equal(root.get("jobCard").get("entityId"), jobCardId);
    }

    default Specification<JobCardTask> jobCardTaskIdIs(String jobCardTaskId) {
        return (root, query, cb) -> cb.equal(root.get("entityId"), jobCardTaskId);
    }

}
