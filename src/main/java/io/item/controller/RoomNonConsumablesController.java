package io.item.controller;

import io.item.form.NonConsumableRoomItemEdit;
import io.item.service.NonConsumableRoomItemEditService;
import io.item.service.NonConsumablesRoomItemReadService;
import io.item.view.RoomNonConsumablesView;
import io.lib.service.Message;
import io.lib.view.ApiResponse;
import io.lib.view.EntityApiResponse;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static io.lib.service.SystemConfig.INTERNAL_USER_BASE_URL;

@RestController
@RequestMapping(INTERNAL_USER_BASE_URL + "/room-nonconsumables")
public class RoomNonConsumablesController {

    private NonConsumableRoomItemEditService nonConsumableRoomItemEditService;

    private NonConsumablesRoomItemReadService nonConsumablesRoomItemReadService;


    @PreAuthorize("hasAuthority('REGISTER_ROOM_NON_CONSUMABLE_ITEM')")
    @PostMapping("{roomEntityId}")
    public EntityApiResponse<RoomNonConsumablesView> addItem(
            @PathVariable String roomEntityId,
            @RequestBody @Valid NonConsumableRoomItemEdit form,
            Authentication auth,
            Locale locale
    ) {

        form.setSessionUserId(auth.getName());

        var roomItem = nonConsumableRoomItemEditService.addItem( roomEntityId,form );

        var response = new RoomNonConsumablesView( roomEntityId,List.of(
                        new RoomNonConsumablesView.Item(
                                roomItem.getNonConsumableItem().getEntityId(),
                                roomItem.getQuantity()
                        )
                )
        );

        return new EntityApiResponse<>(Message.get("room.nonconsumable.item.added.success",locale),response
        );
    }


    @PreAuthorize("hasAuthority('VIEW_ROOM_NON_CONSUMABLE_ITEM')")
    @GetMapping("fetch/{roomEntityId}")
    public EntityApiResponse<RoomNonConsumablesView> list(
            @PathVariable String roomEntityId
    ) {

        var roomItems = nonConsumablesRoomItemReadService .findByRoomId(roomEntityId);

        List<RoomNonConsumablesView.Item> items = new ArrayList<>();
        for (var roomItem : roomItems) {
                items.add( new RoomNonConsumablesView.Item(
                            roomItem.getNonConsumableItem().getEntityId(),
                            roomItem.getQuantity()
                    )
            );
        }

        return new EntityApiResponse<>(new RoomNonConsumablesView(
                        roomEntityId,
                        items
                )
        );
    }


    @PreAuthorize("hasAuthority('DELETE_ROOM_NON_CONSUMABLE_ITEM')")
    @DeleteMapping(
            "{roomEntityId}/{roomNonConsumableItemEntityId}"
    )
    public ApiResponse deleteItem(
            @PathVariable String roomEntityId,
            @PathVariable String roomNonConsumableItemEntityId,
            Authentication auth,
            Locale locale
    ) {

        nonConsumableRoomItemEditService.deleteItem(
                roomEntityId,
                roomNonConsumableItemEntityId,
                auth.getName()
        );

        return new ApiResponse(
                Message.get("room.nonconsumable.item.delete.success",locale)
        );
    }


    @Autowired
    public void setNonConsumableRoomItemEditService(
            NonConsumableRoomItemEditService service
    ) {
        this.nonConsumableRoomItemEditService = service;
    }


    @Autowired
    public void setNonConsumablesRoomItemReadService(
            NonConsumablesRoomItemReadService service
    ) {
        this.nonConsumablesRoomItemReadService = service;
    }
}