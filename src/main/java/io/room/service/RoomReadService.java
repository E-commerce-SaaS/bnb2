package io.room.service;

import io.room.entity.Room;
import io.room.repository.RoomRepository;
import io.lib.service.BaseJpaRepoReadService;
import io.room.form.FetchRoomForm;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;


@Service
public class RoomReadService extends BaseJpaRepoReadService<
        Room, RoomRepository> {

    public Page<Room> listRooms(FetchRoomForm form) {

        var spec = repository.notDeleted();

        if(form.getRoomCategory() != null) {
            spec = spec.and(repository.roomCategoryIs(form.getRoomCategory()));
        }

        return repository.findAll(
            spec,
            repository.defaultPageable(form)
        );
    }

}
