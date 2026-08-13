package io.item.service;

import io.activitylog.form.CreateActivityLogForm;
import io.item.entity.ConsumableItem;
import io.item.form.ConsumableItemEditForm;
import io.item.form.ConsumableItemRegistrationForm;
import io.item.repository.ConsumableItemRepository;
import io.lib.exception.CommonRuntimeException;
import io.lib.exception.ExceptionType;
import io.lib.service.BaseJpaRepoEditService;
import org.springframework.stereotype.Service;

@Service
public class ConsumableItemEditService extends BaseJpaRepoEditService<ConsumableItem, ConsumableItemRepository> {

    public ConsumableItem register(ConsumableItemRegistrationForm form){

        var item = new ConsumableItem();
        item.setName(form.getName());
        item.setDescription(form.getDescription());
        item.setUnitOfMeasure(form.getUnitOfMeasure());
        item.setParLevel(form.getParLevel());
        item.setCreatedByEntityId(form.getSessionUserId());

        item = save(item, form.getSessionUserId());

        var activityLogForm = new CreateActivityLogForm();
        activityLogForm.setOwningEntityId(item.getEntityId());
        activityLogForm.setAction("Consumable item creation");
        activityLogForm.setSessionUserId(form.getSessionUserId());
        activityLogQueuingService.enqueueActivityLog(activityLogForm);

        return item;
    }

    public ConsumableItem update(String consumableItemId, ConsumableItemEditForm form){
        checkNameExists(consumableItemId, form.getName());

        var  consumableItem = findByEntityId(consumableItemId);

        consumableItem.setName(form.getName());
        consumableItem.setDescription(form.getDescription());
        consumableItem.setUnitOfMeasure(form.getUnitOfMeasure());
        consumableItem.setParLevel(form.getParLevel());

        consumableItem = save(consumableItem, form.getSessionUserId());

        var activityLogForm = new CreateActivityLogForm();
        activityLogForm.setOwningEntityId(consumableItem.getEntityId());
        activityLogForm.setAction("Consumable item edit");
        activityLogForm.setSessionUserId(form.getSessionUserId());

        activityLogQueuingService.enqueueActivityLog(activityLogForm);

        return consumableItem;
    }

    public void delete(String consumableItemId, String sessionUserId){

        var  consumableItem = findByEntityId(consumableItemId);
        delete(consumableItem, sessionUserId);

        var activityLogForm = new CreateActivityLogForm();
        activityLogForm.setOwningEntityId(consumableItemId);
        activityLogForm.setAction("Consumable item deleted");
        activityLogForm.setSessionUserId(sessionUserId);

        activityLogQueuingService.enqueueActivityLog(activityLogForm);
    }

    private void checkNameExists(String consumableItemId, String name) {
        var specification = repository.notDeleted()
                .and(repository.nameIs(name))
                .and(repository.entityIdNot(consumableItemId));

        boolean exists = repository.exists(specification);

        if (exists) {
            throw new CommonRuntimeException(
                ExceptionType.BAD_REQUEST,
                "error.duplicate.name"
            );
        }
    }
}
