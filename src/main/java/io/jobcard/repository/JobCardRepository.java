package io.jobcard.repository;

import io.jobcard.entity.JobCard;
import io.lib.repository.BaseJpaRepository;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;


@Repository
public interface JobCardRepository extends BaseJpaRepository<JobCard>, JpaSpecificationExecutor<JobCard> {

    default Specification<JobCard> staffEntityIdIs(String staffEntityId) {
        return (root, query, cb) -> cb.equal(root.get("staff").get("entityId"), staffEntityId);
    }

    default Specification<JobCard> hasEntityId(String id) {
        return (root, query, cb) -> cb.equal(root.get("entityId"), id);
    }

    default Specification<JobCard> notDeleted() {
        return (root, query, cb) -> cb.isNull(root.get("deletedAt"));
    }

}
