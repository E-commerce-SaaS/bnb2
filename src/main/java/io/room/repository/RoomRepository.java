package io.room.repository;

import io.room.entity.RoomCategory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

import io.lib.repository.BaseJpaRepository;
import io.room.entity.Room;

@Repository
public interface RoomRepository extends BaseJpaRepository<Room>{
    default Specification<Room> nameIs(String name){
        return (root, cb, cq) -> cq.equal(root.get("name"), name);
    }

    default Specification<Room> roomCategoryIs(RoomCategory roomCategory){
        return (root, cb, cq) -> cq.equal(root.get("roomCategory"), roomCategory);
    }
}
