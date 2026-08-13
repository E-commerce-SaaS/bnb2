package io.item.controller;

import io.lib.form.BaseFetchForm;
import io.lib.view.ApiResponse;
import org.springframework.web.bind.annotation.PathVariable;
import io.item.form.ConsumableItemEditForm;
import io.item.form.ConsumableItemRegistrationForm;
import io.item.service.ConsumableItemEditService;
import io.item.service.ConsumableItemReadService;
import io.item.view.ConsumableItemView;
import io.lib.service.Message;
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
@RequestMapping(INTERNAL_USER_BASE_URL + "/items/consumables")
public class ConsumableItemController {

    private ConsumableItemEditService consumableItemEditService;
    private ConsumableItemReadService consumableItemReadService;

    @PreAuthorize("hasAuthority('REGISTER_CONSUMABLE_ITEM')")
    @PostMapping("register")
    public EntityApiResponse<ConsumableItemView> register(
            @RequestBody @Valid ConsumableItemRegistrationForm form,
            Authentication auth,
            Locale locale) {
        form.setSessionUserId(auth.getName());
        var consumableItem = consumableItemEditService.register(form);

        return new EntityApiResponse<>(
            Message.get("consumable.item.registration.success", locale),
            new ConsumableItemView(consumableItem)
        );
    }

    @PreAuthorize("hasAuthority('VIEW_CONSUMABLE_ITEM')")
    @GetMapping("list")
    public PagedEntityApiResponse<ConsumableItemView> list(
            @RequestParam(name="query",required = false)String query,
            @RequestParam(name="pageNum",required = false)Integer pageNum,
            @RequestParam(name="pageSize",required = false)Integer pageSize,
            Authentication auth
    ){
        var form = new BaseFetchForm();

        form.setQuery(query);
        form.setPageNum(pageNum);
        form.setPageSize(pageSize);
        form.setSessionUserId(auth.getName());

        var page = consumableItemReadService.listConsumableItems(form);

        var views = page.getContent()
                .stream()
                .map(ConsumableItemView::new)
                .toList();

        return new PagedEntityApiResponse<>(page, views);
    }

    @PreAuthorize("hasAuthority('EDIT_CONSUMABLE_ITEM')")
    @PutMapping("edit/{consumableItemId}")
    public EntityApiResponse<ConsumableItemView> edit(
            @PathVariable String consumableItemId,
            @RequestBody @Valid ConsumableItemEditForm form,
            Authentication auth,
            Locale locale){
        form.setSessionUserId(auth.getName());

        var consumableItem = consumableItemEditService.update(
                consumableItemId,
                form
        );
        return new EntityApiResponse<>(
                Message.get("consumable.item.edit.success", locale),
                new ConsumableItemView(consumableItem)
        );
    }

    @PreAuthorize("hasAuthority('DELETE_CONSUMABLE_ITEM')")
    @DeleteMapping("delete/{consumableItemId}")
    public ApiResponse delete(
            @PathVariable String consumableItemId,
            Authentication auth,
            Locale locale){
        consumableItemEditService.delete(consumableItemId, auth.getName());
        return new ApiResponse(Message.get("consumable.item.delete.success", locale));
    }

    @Autowired
    public void setConsumableItemEditService(ConsumableItemEditService service) {
        this.consumableItemEditService = service;
    }

    @Autowired
    public void setConsumableItemReadService(ConsumableItemReadService service) {
        this.consumableItemReadService = service;
    }
}

