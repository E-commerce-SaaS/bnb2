package io.room.controller;


import io.lib.view.PagedEntityApiResponse;
import io.room.entity.ReservationStatus;
import io.room.entity.RoomCategory;
import io.room.form.FetchRoomForm;
import io.room.service.RoomReadService;
import io.room.view.RoomView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static io.lib.service.SystemConfig.INTERNAL_USER_BASE_URL;

@RestController
@RequestMapping(INTERNAL_USER_BASE_URL + "/rooms")
public class RoomController {
    private RoomReadService roomReadService;

    @PreAuthorize("hasAuthority('VIEW_ROOM')")
    @GetMapping("list")
    public PagedEntityApiResponse<RoomView> list(
            @RequestParam(name="query", required = false)String query,
            @RequestParam(name="status", required = false)ReservationStatus reservationStatus,
            @RequestParam(name="roomCategory", required = false) RoomCategory roomCategory,
            @RequestParam(name="branchEntityId", required = false)String branchEntityId,
            @RequestParam(name="pageNum", required = false, defaultValue = "0") Integer pageNum,
            @RequestParam(name="pageSize", required = false, defaultValue = "100") Integer pageSize

    ){
        var form = new FetchRoomForm();
        form.setQuery(query);
        form.setReservationStatus(reservationStatus);
        form.setRoomCategory(roomCategory);
        form.setBranchEntityId(branchEntityId);
        form.setPageNum(pageNum);
        form.setPageSize(pageSize);

        var page = roomReadService.listRooms(form);
        var views = page.getContent().stream().map(RoomView::new).toList();
        return new PagedEntityApiResponse<>(page, views);
    }

    @Autowired
    public void setRoomReadService(RoomReadService service) {
        this.roomReadService = service;
    }
}
