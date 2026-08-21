package io.room.repository;

import io.room.entity.ReservationStatus;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

import io.lib.repository.BaseJpaRepository;
import io.room.entity.Room;

@Repository
public interface RoomRepository extends BaseJpaRepository<Room>{
    default Specification<Room> nameIs(String name){
        return (root, cb, cq) -> cq.equal(root.get("name"), name);
    }

    default Specification<Room> nameLike(String query){
        return (root, cb, cq) -> cq.like(root.get("name"), "%" + query + "%");
    }

    default Specification<Room> roomCategoryIdIs(String roomCategoryId){
        return (root, cb, cq) -> cq.equal(root.get("roomCategoryEntityId"), roomCategoryId);
    }

    default Specification<Room> reservationStatusIs(ReservationStatus  reservationStatus){
        return ( root, cb, cq) -> cq.equal(root.get("reservationStatus"), reservationStatus);
    }

    default Specification<Room> orgBranchIdIs(String orgBranchId){
        return ( root, cb, cq) -> cq.equal(root.get("orgBranchEntityId"), orgBranchId);
    }
}
