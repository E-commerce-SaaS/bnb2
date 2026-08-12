package io.housekeeping.service;

import io.activitylog.form.CreateActivityLogForm;
import io.housekeeping.entity.ConsumableItem;
import io.housekeeping.form.ConsumableItemEditForm;
import io.housekeeping.form.ConsumableItemRegistrationForm;
import io.housekeeping.repository.ConsumableItemRepository;
import io.lib.exception.CommonRuntimeException;
import io.lib.exception.ExceptionType;
import io.lib.service.BaseJpaRepoEditService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ConsumableItemEditService extends BaseJpaRepoEditService<ConsumableItem, ConsumableItemRepository> {

    private ConsumableItemRepository consumableItemRepository;

    public ConsumableItem register(ConsumableItemRegistrationForm form){

        var consumableItem = new ConsumableItem();

        consumableItem.setName(form.getName());
        consumableItem.setDescription(form.getDescription());
        consumableItem.setUnitOfMeasure(form.getUnitOfMeasure());
        consumableItem.setParLevel(form.getParLevel());

        consumableItem.setCreatedByEntityId(form.getSessionUserId());

        consumableItem = save(consumableItem, form.getSessionUserId()
        );

        var activityLogForm = new CreateActivityLogForm();
        activityLogForm.setOwningEntityId(consumableItem.getEntityId());
        activityLogForm.setAction("Consumable item creation");
        activityLogForm.setSessionUserId(form.getSessionUserId());

        return consumableItem;

    }

    public ConsumableItem update(String consumableItemId, ConsumableItemEditForm form){
        checkNameExists(consumableItemId, form.getName());

        var  consumableItem = findByEntityId(consumableItemId);

        consumableItem.setName(form.getName());
        consumableItem.setDescription(form.getDescription());
        consumableItem.setUnitOfMeasure(form.getUnitOfMeasure());
        consumableItem.setParLevel(form.getParLevel());

        consumableItem = save(
                consumableItem,
                form.getSessionUserId()
        );

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
        activityLogForm.setOwningEntityId(consumableItem.getEntityId());
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

    @Autowired
    public void setConsumableItemRepository(ConsumableItemRepository consumableItemRepository) {
        this.consumableItemRepository = consumableItemRepository;
    }
}
