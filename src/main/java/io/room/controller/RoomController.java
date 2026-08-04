package io.room.controller;

import io.lib.service.Message;
import io.lib.view.EntityApiResponse;
import io.room.form.RoomEditForm;
import io.room.form.RoomRegistrationForm;
import io.room.service.RoomEditService;
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

import java.util.Locale;

import static io.lib.service.SystemConfig.INTERNAL_USER_BASE_URL;

@RestController
@RequestMapping(INTERNAL_USER_BASE_URL + "/rooms")
public class RoomController {
    private RoomEditService roomEditService;

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
    @PutMapping(INTERNAL_USER_BASE_URL + "/room_update")
    public EntityApiResponse<RoomView> updateRoom(

        @PathVariable String entityId,
        @RequestBody @Valid RoomEditForm form,
        Authentication auth,
        Locale locale) {

        form.setSessionUserId(auth.getName());
        var room = roomEditService.updateRoom(form, entityId);

         return new EntityApiResponse<>(
            Message.get("room.update.success", locale),
            new RoomView(room)
    );
}

    @Autowired
    public void setRoomEditService(RoomEditService service) {
        this.roomEditService = service;
    }
}
