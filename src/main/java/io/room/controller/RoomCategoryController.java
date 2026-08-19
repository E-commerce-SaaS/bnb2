package io.room.controller;

import io.lib.form.BaseFetchForm;
import io.lib.view.EntityApiResponse;
import io.lib.view.PagedEntityApiResponse;
import io.room.service.RoomCategoryReadService;
import io.room.view.RoomCategoryView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static io.lib.service.SystemConfig.INTERNAL_USER_BASE_URL;

@RestController
@RequestMapping(INTERNAL_USER_BASE_URL + "/room-categories")
public class RoomCategoryController {
    private RoomCategoryReadService roomCategoryReadService;

    @PreAuthorize("hasAuthority('VIEW_ROOM_CATEGORY')")
    @GetMapping("list")
    public PagedEntityApiResponse<RoomCategoryView> list(
        @RequestParam(name="query", required = false)String query,
        @RequestParam(name="pageNum", required = false, defaultValue = "0") Integer pageNum,
        @RequestParam(name="pageSize", required = false, defaultValue = "100") Integer pageSize
    ){
        var form = new BaseFetchForm();
        form.setQuery(query);
        form.setPageNum(pageNum);
        form.setPageSize(pageSize);

        var page = roomCategoryReadService.listCategories(form);
        var views = page.getContent().stream().map(RoomCategoryView::new).toList();
        return new PagedEntityApiResponse<>(page, views);
    }

    @PreAuthorize("hasAuthority('VIEW_ROOM_CATEGORY')")
    @GetMapping("fetch/{roomCategoryId}")
    public EntityApiResponse<RoomCategoryView> fetch(@PathVariable String roomCategoryId){
        var category = roomCategoryReadService.findByEntityId(roomCategoryId);
        return new EntityApiResponse<>(new RoomCategoryView(category));
    }

    @Autowired
    public void setRoomCategoryReadService(RoomCategoryReadService roomCategoryReadService) {
        this.roomCategoryReadService = roomCategoryReadService;
    }
}
