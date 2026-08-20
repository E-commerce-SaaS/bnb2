package io.jobcard.repository;

import io.jobcard.entity.JobCard;
import io.jobcard.entity.JobCardTask;
import io.lib.repository.BaseJpaRepository;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;



@Repository
public interface JobCardRepository extends BaseJpaRepository<JobCard>{

    default Specification<JobCard> staffEntityIdIs(String staffEntityId) {
        return (root, query, cb) -> cb.equal(root.get("staff").get("entityId"), staffEntityId);
    }

    default Specification<JobCard> hasEntityId(String id) {
        return (root, query, cb) -> cb.equal(root.get("entityId"), id);
    }

}
