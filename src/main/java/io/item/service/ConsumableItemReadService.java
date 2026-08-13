package io.item.service;

import io.item.entity.ConsumableItem;
import io.item.repository.ConsumableItemRepository;
import io.lib.form.BaseFetchForm;
import io.lib.service.BaseJpaRepoReadService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
public class ConsumableItemReadService extends BaseJpaRepoReadService<ConsumableItem, ConsumableItemRepository> {

    public Page<ConsumableItem> listConsumableItems(BaseFetchForm form){

        var specification = repository.notDeleted();

        if(form.getQuery() != null && !form.getQuery().isBlank()){

            specification = specification.and(repository.nameIs(form.getQuery())
            );
        }

        return repository.findAll(
                specification,
                repository.defaultPageable(form)
        );
    }
}
