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

        var pageable = repository.defaultPageable(form);

        return repository.findAll(pageable);
    }

}
