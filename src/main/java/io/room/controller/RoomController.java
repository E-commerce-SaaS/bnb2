package io.room.controller;

import io.lib.service.Message;
import io.lib.view.EntityApiResponse;
import io.room.form.RoomEditForm;
import io.lib.view.PagedEntityApiResponse;
import io.room.entity.ReservationStatus;
import io.room.entity.RoomCategory;
import io.room.form.FetchRoomForm;
import io.room.form.RoomRegistrationForm;
import io.room.service.RoomEditService;
import io.room.service.RoomReadService;
import io.room.view.RoomView;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Locale;

import static io.lib.service.SystemConfig.INTERNAL_USER_BASE_URL;

@RestController
@RequestMapping(INTERNAL_USER_BASE_URL + "/rooms")
public class RoomController {
    private RoomEditService roomEditService;
    private RoomReadService roomReadService;

    @PreAuthorize("hasAuthority('REGISTER_ROOM')")
    @PostMapping("register")
    public EntityApiResponse<RoomView> registerRoom(
            @RequestBody @Valid RoomRegistrationForm form,
            Authentication auth,
            Locale locale) {
        form.setSessionUserId(auth.getName());
        var room = roomEditService.registerRoom(form);

        return new EntityApiResponse<>(
            Message.get("room.registration.success", locale),
            new RoomView(room)
        );
    }


    @PreAuthorize("hasAuthority('UPDATE_ROOM')")
    @PutMapping(INTERNAL_USER_BASE_URL + "/room_update/{roomId}")
    public EntityApiResponse<RoomView> updateRoom(
        @PathVariable String roomId,
        @RequestBody @Valid RoomEditForm form,
        Authentication auth,
        Locale locale) {

        form.setSessionUserId(auth.getName());
        var room = roomEditService.updateRoom(roomId, form);

         return new EntityApiResponse<>(
            Message.get("room.update.success", locale),
            new RoomView(room)
    );
}

    @PreAuthorize("hasAuthority('VIEW_ROOM')")
    @GetMapping("list")
    public PagedEntityApiResponse<RoomView> list(
            @RequestParam(name="query", required = false)String query,
            @RequestParam(name="status", required = false) ReservationStatus reservationStatus,
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
    public void setRoomEditService(RoomEditService service) {
        this.roomEditService = service;
    }

    @Autowired
    public void setRoomReadService(RoomReadService service) {
        this.roomReadService = service;
    }
}
