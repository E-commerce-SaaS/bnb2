package io.room.repository;

import org.springframework.stereotype.Repository;

import io.lib.repository.BaseJpaRepository;
import io.room.entity.Room;

@Repository
public interface RoomRepository extends BaseJpaRepository<Room>{

}
