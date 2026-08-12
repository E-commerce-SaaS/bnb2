package io.task.repository;

import io.lib.repository.BaseJpaRepository;
import io.task.entity.Task;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends BaseJpaRepository<Task> {

    default Specification<Task> taskTitleIs(String title){
        return (root, cb, cq) -> cq.equal(root.get("taskTitle"), title);
    }

    default Specification<Task> taskTitleLike(String keyword){
        return (root, cq, cb) -> cb.like(root.get("name"), "%" + keyword +"%");
    }

}



