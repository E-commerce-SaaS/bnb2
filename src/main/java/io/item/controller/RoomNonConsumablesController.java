package io.item.controller;

import io.item.form.ChildRoomNonConsumableItemEdit;
import io.item.form.RoomNonConsumableItemEdit;
import io.item.service.ChildRoomNonConsumableItemEditService;
import io.item.service.RoomNonConsumablesEditService;
import io.item.service.RoomNonConsumablesReadService;
import io.item.view.RoomNonConsumablesView;
import io.lib.service.Message;
import io.lib.view.ApiResponse;
import io.lib.view.EntityApiResponse;

import jakarta.validation.Valid;

import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;

import static io.lib.service.SystemConfig.INTERNAL_USER_BASE_URL;


@RestController
@RequestMapping( INTERNAL_USER_BASE_URL + "/room-nonconsumables")
public class RoomNonConsumablesController {

    private RoomNonConsumablesEditService roomNonConsumablesEditService;

    private ChildRoomNonConsumableItemEditService childRoomNonConsumableItemEditService;

    private RoomNonConsumablesReadService roomNonConsumablesReadService;


    @PreAuthorize("hasAuthority('REGISTER_ROOM_NON_CONSUMABLE_ITEM')")
    @PostMapping("register")
    public EntityApiResponse<RoomNonConsumablesView> register(
            @RequestBody @Valid RoomNonConsumableItemEdit form,
            Authentication auth,
            Locale locale
    ) throws BadRequestException {
        form.setSessionUserId(auth.getName());
        var roomNonConsumables =roomNonConsumablesEditService.register(form);
        return new EntityApiResponse<>(
                Message.get("room.non-consumable.item.registration.success",locale),
                new RoomNonConsumablesView(roomNonConsumables)
        );
    }


    @PreAuthorize("hasAuthority('REGISTER_ROOM_NON_CONSUMABLE_ITEM')")
    @PostMapping("{roomEntityId}/items")
    public EntityApiResponse<RoomNonConsumablesView> addItem(
            @PathVariable String roomEntityId,
            @RequestBody @Valid ChildRoomNonConsumableItemEdit form,
            Authentication auth,
            Locale locale
    ) {

        form.setSessionUserId(auth.getName());
        var roomItem =childRoomNonConsumableItemEditService.addItem( roomEntityId,form);
        return new EntityApiResponse<>(
                Message.get("room.nonconsumable.item.added.success", locale),
                new RoomNonConsumablesView(roomItem.getRoomNonConsumables())
        );
    }



    @PreAuthorize("hasAuthority('VIEW_ROOM_NON_CONSUMABLE_ITEM')")
    @GetMapping("fetch/{roomEntityId}")
    public EntityApiResponse<RoomNonConsumablesView> list(
            @PathVariable String roomEntityId
    ) {

        var roomNonConsumables =
                roomNonConsumablesReadService.findByRoom(
                        roomEntityId
                );

        return new EntityApiResponse<>(
                new RoomNonConsumablesView(
                        roomNonConsumables
                )
        );
    }

    @PreAuthorize("hasAuthority('DELETE_ROOM_NON_CONSUMABLE_ITEM')" )
    @DeleteMapping(
            "{roomEntityId}/items/{roomNonConsumableItemEntityId}"
    )
    public ApiResponse deleteItem(
            @PathVariable String roomEntityId,
            @PathVariable String roomNonConsumableItemEntityId,
            Authentication auth,
            Locale locale
    ) {

        childRoomNonConsumableItemEditService.deleteItem( roomEntityId, roomNonConsumableItemEntityId,auth.getName());
        return new ApiResponse(
                Message.get(
                        "room.nonconsumable.item.delete.success",
                        locale
                )
        );
    }



    @Autowired
    public void setRoomNonConsumablesEditService(
            RoomNonConsumablesEditService service
    ) {

        this.roomNonConsumablesEditService = service;
    }


    @Autowired
    public void setchildRoomNonConsumableItemEditService(ChildRoomNonConsumableItemEditService service) {
        this.childRoomNonConsumableItemEditService = service;
    }


    @Autowired
    public void setRoomNonConsumablesReadService( RoomNonConsumablesReadService service) {
        this.roomNonConsumablesReadService = service;
    }
}