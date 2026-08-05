package io.room.service;

import io.internaluser.service.InternalUserReadService;
import io.micrometer.common.util.StringUtils;
import io.room.entity.Room;
import io.room.repository.RoomRepository;
import io.lib.service.BaseJpaRepoReadService;
import io.room.form.FetchRoomForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;


@Service
public class RoomReadService extends BaseJpaRepoReadService<
        Room, RoomRepository> {

    private InternalUserReadService internalUserReadService;

    public Page<Room> listRooms(FetchRoomForm form) {

        var spec = repository.notDeleted();

        if(form.getRoomCategory() != null) {
            spec = spec.and(repository.roomCategoryIs(form.getRoomCategory()));
        }

        if(form.getReservationStatus() != null) {
            spec = spec.and(repository.reservationStatusIs(form.getReservationStatus()));
        }

        if (StringUtils.isNotBlank(form.getBranchEntityId())) {
            spec = spec.and(repository.branchEntityIdIs(form.getBranchEntityId()));
        }

        if(StringUtils.isNotBlank(form.getSessionUserId())){
            var user = internalUserReadService.findByEntityId(form.getSessionUserId());
            if(user.getOrgBranch() != null){
                spec = spec.and(repository.branchEntityIdIs(user.getOrgBranch().getEntityId()));
            }
        }

        return repository.findAll(
            spec,
            repository.defaultPageable(form)
        );
    }

    @Autowired
    public void setInternalUserReadService(InternalUserReadService service) {
        this.internalUserReadService = service;
    }
}
