package io.room.service;


import io.lib.form.BaseFetchForm;
import io.lib.service.BaseJpaRepoReadService;
import io.micrometer.common.util.StringUtils;
import io.room.entity.RoomCategory;
import io.room.repository.RoomCategoryRepository;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
public class RoomCategoryReadService extends BaseJpaRepoReadService<RoomCategory, RoomCategoryRepository> {

    public Page<RoomCategory> listCategories(BaseFetchForm form){
        var spec = repository.notDeleted();
        if(StringUtils.isNotBlank(form.getQuery())){
            spec = spec.and(repository.nameLike(form.getQuery()));
        }

        return repository.findAll(spec, repository.defaultPageable(form));
    }
}
