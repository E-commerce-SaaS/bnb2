package io.item.controller;

import io.item.form.RoomItemFetchForm;
import io.item.form.RoomNonConsumableItemEditForm;
import io.item.service.NonConsumableRoomItemEditService;
import io.item.service.NonConsumablesRoomItemReadService;
import io.item.view.NonConsumableRoomItemView;
import io.lib.form.SessionUserIdForm;
import io.lib.service.Message;
import io.lib.view.ApiResponse;
import io.lib.view.EntityApiResponse;

import io.lib.view.PagedEntityApiResponse;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;

import static io.lib.service.SystemConfig.INTERNAL_USER_BASE_URL;

@RestController
@RequestMapping(INTERNAL_USER_BASE_URL + "/room-nonconsumables")
public class RoomNonConsumablesController {

    private NonConsumableRoomItemEditService nonConsumableRoomItemEditService;
    private NonConsumablesRoomItemReadService nonConsumablesRoomItemReadService;

    @PreAuthorize("hasAuthority('REGISTER_ROOM_NON_CONSUMABLE_ITEM')")
    @PostMapping("add/{roomEntityId}")
    public EntityApiResponse<NonConsumableRoomItemView> addItem(
        @PathVariable String roomEntityId,
        @RequestBody @Valid RoomNonConsumableItemEditForm form,
        Authentication auth,
        Locale locale) {

        form.setSessionUserId(auth.getName());
        var roomItem = nonConsumableRoomItemEditService.addItem(roomEntityId, form);

        return new EntityApiResponse<>(
            Message.get("item.addition.success",locale),
            new NonConsumableRoomItemView(roomItem)
        );
    }

    @PreAuthorize("hasAuthority('DELETE_ROOM_NON_CONSUMABLE_ITEM')")
    @PostMapping("delete/{roomItemEntityId}")
    public ApiResponse deleteItem(
        @PathVariable String roomItemEntityId,
        Authentication auth,
        Locale locale
    ) {
        var form = new SessionUserIdForm();
        form.setSessionUserId(auth.getName());

        nonConsumableRoomItemEditService.deleteItem(roomItemEntityId, form);

        return new ApiResponse(
                Message.get("room.nonconsumable.item.delete.success",locale)
        );
    }

    @PreAuthorize("hasAuthority('VIEW_ROOM_NON_CONSUMABLE_ITEM')")
    @GetMapping("list/{roomEntityId}")
    public PagedEntityApiResponse<NonConsumableRoomItemView> list(
            @PathVariable String roomEntityId,
            @RequestParam(value = "pageNum", required = false, defaultValue = "0") Integer pageNum,
            @RequestParam(value = "pageSize", required = false, defaultValue = "100") Integer pageSize) {

        var form = new RoomItemFetchForm();
        form.setRoomEntityId(roomEntityId);
        form.setPageNum(pageNum);
        form.setPageSize(pageSize);
        var page = nonConsumablesRoomItemReadService.list(form);
        var views = page.stream().map(NonConsumableRoomItemView::new).toList();
        return new PagedEntityApiResponse<>(page, views);
    }

    @Autowired
    public void setNonConsumableRoomItemEditService(NonConsumableRoomItemEditService service) {
        this.nonConsumableRoomItemEditService = service;
    }

    @Autowired
    public void setNonConsumablesRoomItemReadService(NonConsumablesRoomItemReadService service) {
        this.nonConsumablesRoomItemReadService = service;
    }
}