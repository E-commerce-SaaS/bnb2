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

        if(StringUtils.isNotBlank(form.getQuery())){
            spec = spec.and(repository.nameLike(form.getQuery()));
        }

        if(form.getRoomCategoryId() != null) {
            spec = spec.and(repository.roomCategoryIdIs(form.getRoomCategoryId()));
        }

        if(form.getReservationStatus() != null) {
            spec = spec.and(repository.reservationStatusIs(form.getReservationStatus()));
        }

        if (StringUtils.isNotBlank(form.getOrgBranchId())) {
            spec = spec.and(repository.orgBranchIdIs(form.getOrgBranchId()));
        }

        if(StringUtils.isNotBlank(form.getSessionUserId())){
            var user = internalUserReadService.findByEntityId(form.getSessionUserId());
            if(user.getOrgBranch() != null){
                spec = spec.and(repository.orgBranchIdIs(user.getOrgBranch().getEntityId()));
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
