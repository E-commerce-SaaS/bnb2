package io.item.controller;

import io.lib.form.BaseFetchForm;
import io.lib.view.ApiResponse;
import io.item.form.ConsumableItemEditForm;
import io.item.form.ConsumableItemRegistrationForm;
import io.item.form.NonConsumableEditForm;
import io.item.form.NonConsumableItemRegistrationForm;
import io.item.service.ConsumableItemEditService;
import io.item.service.ConsumableItemReadService;
import io.item.service.NonConsumableItemEditService;
import io.item.service.NonConsumableItemReadService;
import io.item.view.NonConsumableItemView;
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
@RequestMapping(INTERNAL_USER_BASE_URL + "/items/nonconsumables")
public class NonConsumableItemController {

    private NonConsumableItemEditService nonConsumableItemEditService;
    private NonConsumableItemReadService nonConsumableItemReadService;

    @PreAuthorize("hasAuthority('REGISTER_NON_CONSUMABLE_ITEM')")
    @PostMapping("register")
    public EntityApiResponse<NonConsumableItemView> register(
            @RequestBody @Valid NonConsumableItemRegistrationForm form,
            Authentication auth,
            Locale locale) {
        form.setSessionUserId(auth.getName());
        var nonConsumableItem = nonConsumableItemEditService.register(form);

        return new EntityApiResponse<>(
            Message.get("non-consumable.item.registration.success", locale),
            new NonConsumableItemView(nonConsumableItem)
        );
    }

    @PreAuthorize("hasAuthority('VIEW_NON_CONSUMABLE_ITEM')")
    @GetMapping("list")
    public PagedEntityApiResponse<NonConsumableItemView> list(
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

        var page = nonConsumableItemReadService.listNonConsumableItems(form);

        var views = page.getContent()
                .stream()
                .map(NonConsumableItemView::new)
                .toList();

        return new PagedEntityApiResponse<>(page, views);
    }

    @PreAuthorize("hasAuthority('EDIT_NON_CONSUMABLE_ITEM')")
    @PutMapping("edit/{nonConsumableItemId}")
    public EntityApiResponse<NonConsumableItemView> edit(
            @PathVariable String nonConsumableItemId,
            @RequestBody @Valid NonConsumableEditForm form,
            Authentication auth,
            Locale locale){
        form.setSessionUserId(auth.getName());

        var nonConsumableItem = nonConsumableItemEditService.update(
                nonConsumableItemId,
                form
        );
        return new EntityApiResponse<>(
                Message.get("nonconsumable.item.edit.success", locale),
                new NonConsumableItemView(nonConsumableItem)
        );
    }

    @PreAuthorize("hasAuthority('DELETE_NON_CONSUMABLE_ITEM')")
    @DeleteMapping("delete/{nonConsumableItemId}")
    public ApiResponse delete(
            @PathVariable String nonConsumableItemId,
            Authentication auth,
            Locale locale){
        nonConsumableItemEditService.delete(nonConsumableItemId, auth.getName());
        return new ApiResponse(Message.get("nonconsumable.item.delete.success", locale));
    }

    @Autowired
    public void setNonConsumableItemEditService(NonConsumableItemEditService nonConsumableItemEditService) {
        this.nonConsumableItemEditService = nonConsumableItemEditService;
    }

    @Autowired
    public void setNonConsumableItemReadService(NonConsumableItemReadService nonConsumableItemEditService) {
        this.nonConsumableItemReadService = nonConsumableItemEditService;
    }
}

