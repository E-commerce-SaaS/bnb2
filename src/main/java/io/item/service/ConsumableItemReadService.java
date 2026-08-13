package io.item.service;

import io.item.entity.ConsumableItem;
import io.item.repository.ConsumableItemRepository;
import io.lib.form.BaseFetchForm;
import io.lib.service.BaseJpaRepoReadService;
import org.apache.commons.lang.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
public class ConsumableItemReadService extends BaseJpaRepoReadService<ConsumableItem, ConsumableItemRepository> {

    public Page<ConsumableItem> listConsumableItems(BaseFetchForm form){
        var specification = repository.notDeleted();

        if(StringUtils.isNotBlank(form.getQuery())){
            specification = specification.and(repository.nameContains(form.getQuery()));
        }

        return repository.findAll(
            specification,
            repository.defaultPageable(form)
        );
    }
}
