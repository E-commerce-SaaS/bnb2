package io.item.service;


import io.activitylog.form.CreateActivityLogForm;

import io.item.entity.NonConsumableItem;
import io.item.form.NonConsumableEditForm;
import io.item.form.NonConsumableItemRegistrationForm;
import io.item.repository.NonConsumableItemRepository;
import io.lib.exception.CommonRuntimeException;
import io.lib.exception.ExceptionType;
import io.lib.service.BaseJpaRepoEditService;
import org.springframework.stereotype.Service;

@Service
public class NonConsumableItemEditService extends BaseJpaRepoEditService<NonConsumableItem, NonConsumableItemRepository> {

    public NonConsumableItem register(NonConsumableItemRegistrationForm form){

        var item = new NonConsumableItem();
        item.setName(form.getName());
        item.setDescription(form.getDescription());
        item.setCreatedByEntityId(form.getSessionUserId());

        item = save(item, form.getSessionUserId());

        var activityLogForm = new CreateActivityLogForm();
        activityLogForm.setOwningEntityId(item.getEntityId());
        activityLogForm.setAction("NonConsumable item creation");
        activityLogForm.setSessionUserId(form.getSessionUserId());
        activityLogQueuingService.enqueueActivityLog(activityLogForm);

        return item;
    }

    public NonConsumableItem update(String nonConsumableItemId, NonConsumableEditForm form){
        checkNameExists(nonConsumableItemId, form.getName());

        var  consumableItem = findByEntityId(nonConsumableItemId);
        consumableItem.setName(form.getName());
        consumableItem.setDescription(form.getDescription());
        consumableItem = save(consumableItem, form.getSessionUserId());

        var activityLogForm = new CreateActivityLogForm();
        activityLogForm.setOwningEntityId(consumableItem.getEntityId());
        activityLogForm.setAction("NonConsumable item edit");
        activityLogForm.setSessionUserId(form.getSessionUserId());

        activityLogQueuingService.enqueueActivityLog(activityLogForm);

        return consumableItem;
    }

    public void delete(String nonConsumableItemId, String sessionUserId){

        var  consumableItem = findByEntityId(nonConsumableItemId);
        delete(consumableItem, sessionUserId);

        var activityLogForm = new CreateActivityLogForm();
        activityLogForm.setOwningEntityId(nonConsumableItemId);
        activityLogForm.setAction("NonConsumable item deleted");
        activityLogForm.setSessionUserId(sessionUserId);

        activityLogQueuingService.enqueueActivityLog(activityLogForm);
    }

    private void checkNameExists(String nonConsumableItemId, String name) {
        var specification = repository.notDeleted()
                .and(repository.nameIs(name))
                .and(repository.entityIdNot(nonConsumableItemId));

        boolean exists = repository.exists(specification);

        if (exists) {
            throw new CommonRuntimeException(
                ExceptionType.BAD_REQUEST,
                "error.duplicate.name"
            );
        }
    }
}
