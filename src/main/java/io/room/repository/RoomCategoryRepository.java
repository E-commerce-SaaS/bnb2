package io.room.repository;

import io.lib.repository.BaseJpaRepository;
import io.room.entity.RoomCategory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;


@Repository
public interface RoomCategoryRepository extends BaseJpaRepository<RoomCategory> {
    default Specification<RoomCategory> nameLike(String query){
        return (root, cq, cb) -> cb.like(root.get("name"), "%" + query + "%");
    }
}
