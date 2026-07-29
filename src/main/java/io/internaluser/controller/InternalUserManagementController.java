package io.internaluser.controller;

import io.internaluser.entity.InternalUser;
import io.internaluser.form.InternalUserEditForm;
import io.internaluser.form.InternalUserOrgBranchUpdateForm;
import io.internaluser.form.InternalUserOrgDepartmentUpdateForm;
import io.internaluser.form.InternalUserRegistrationForm;
import io.internaluser.service.InternalUserAuthGroupReadService;
import io.internaluser.service.InternalUserEditService;
import io.internaluser.service.InternalUserReadService;
import io.internaluser.validator.RequiredInternalUserStatus;
import io.internaluser.view.InternalUserAuthView;
import io.internaluser.view.InternalUserView;
import io.lib.form.RemarksForm;
import io.lib.service.Message;
import io.lib.view.EntityApiResponse;
import io.lib.view.PagedEntityApiResponse;
import io.user.entity.UserStatus;
import io.user.form.FetchUserForm;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;

import static io.lib.service.SystemConfig.INTERNAL_USER_BASE_URL;


@RestController
@RequestMapping(INTERNAL_USER_BASE_URL +"/users")
@Validated
public class InternalUserManagementController {
    private InternalUserEditService internalUserEditService;
    private InternalUserReadService internalUserReadService;
    private InternalUserAuthGroupReadService internalUserAuthGroupReadService;

    @PreAuthorize("hasAuthority('REGISTER_INTERNAL_USER')")
    @PostMapping("register")
    public EntityApiResponse<InternalUserAuthView> register(
            @RequestBody @Valid InternalUserRegistrationForm form,
            Authentication auth,
            Locale locale){
        form.setSessionUserId(auth.getName());
        var internalUser = internalUserEditService.register(form);
        return new EntityApiResponse<>(
            Message.get("user.registration.success", locale),
            new InternalUserAuthView(internalUser)
        );
    }

    @PreAuthorize("hasAuthority('EDIT_INTERNAL_USER')")
    @PostMapping("edit/{internalUserId}")
    public EntityApiResponse<InternalUserAuthView> edit(
            @PathVariable String internalUserId,
            @RequestBody
            @Valid InternalUserEditForm form,
            Authentication auth,
            Locale locale){
        form.setSessionUserId(auth.getName());
        InternalUser internalUser = internalUserEditService.edit(internalUserId, form);
        return new EntityApiResponse<>(
            Message.get("user.edit.success", locale),
            new InternalUserAuthView(internalUser)
        );
    }

    @PreAuthorize("hasAuthority('SUSPEND_INTERNAL_USER')")
    @PostMapping("suspend/{internalUserId}")
    public EntityApiResponse<InternalUserAuthView> suspend(
            @PathVariable
            @RequiredInternalUserStatus(
                statuses = {UserStatus.ACTIVE},
                message = "error.user.not.active"
            )
            String internalUserId,
            @RequestBody @Valid RemarksForm form,
            Authentication auth,
            Locale locale){
        form.setSessionUserId(auth.getName());
        InternalUser user = internalUserEditService.suspend(internalUserId, form);
        return new EntityApiResponse<>(
            Message.get("user.suspension.success", locale),
            internalUserReadService.getInternalUserAuthView(user)
        );
    }

    @PreAuthorize("hasAuthority('ACTIVATE_INTERNAL_USER')")
    @PostMapping("activate/{id}")
    public EntityApiResponse<InternalUserAuthView> activate(
            @PathVariable(value = "id")
            @RequiredInternalUserStatus(
                statuses = {UserStatus.PENDING_APPROVAL, UserStatus.SUSPENDED},
                message = "error.user.already.active"
            )
            String internalUserId,
            @RequestBody @Valid RemarksForm form,
            Authentication auth,
            Locale locale){
        form.setSessionUserId(auth.getName());
        InternalUser user = internalUserEditService.activate(internalUserId, form);
        return new EntityApiResponse<>(
            Message.get("user.activation.success", locale),
            internalUserReadService.getInternalUserAuthView(user)
        );
    }

    @PreAuthorize("hasAuthority('VIEW_INTERNAL_USER')")
    @GetMapping(value = "list")
    public PagedEntityApiResponse<InternalUserAuthView> listUsers(
            @RequestParam(value = "query", required = false) String query,
            @RequestParam(value = "status", required = false) UserStatus status,
            @RequestParam(value = "pageNum", required = false, defaultValue = "0") Integer pageNum,
            @RequestParam(value = "pageSize", required = false, defaultValue = "100") Integer pageSize){

        FetchUserForm form = new FetchUserForm();
        form.setQuery(query);
        form.setUserStatus(status);
        form.setPageNum(pageNum);
        form.setPageSize(pageSize);

        Page<InternalUser>  page = internalUserReadService.listUsers(form);

        var views = page.stream()
                .map(user -> internalUserReadService.getInternalUserAuthView(user))
                .toList();

        return new PagedEntityApiResponse<>(page, views);
    }

    @GetMapping(value = "list", params = {"authGroupId"})
    public PagedEntityApiResponse<InternalUserAuthView> listUsers(
            @RequestParam(value = "authGroupId") String authGroupId,
            @RequestParam(value = "pageNum", required = false, defaultValue = "0") Integer pageNum,
            @RequestParam(value = "pageSize", required = false, defaultValue = "100") Integer pageSize){
        FetchUserForm form = new FetchUserForm();
        form.setPageNum(pageNum);
        form.setPageSize(pageSize);
        var page  = internalUserAuthGroupReadService.listByAuthGroup(form, authGroupId);
        var views = page.stream()
            .map(usrAuthGrp -> internalUserReadService.getInternalUserAuthView(usrAuthGrp.getInternalUser()))
            .toList();

        return new PagedEntityApiResponse<>(page, views);
    }

    @GetMapping("fetch")
    public EntityApiResponse<InternalUserAuthView> fetch(@RequestParam(value = "id") String userEntityId) {
        InternalUser user = internalUserEditService.findByEntityId(userEntityId);
        return new EntityApiResponse<>(internalUserReadService.getInternalUserAuthView(user));
    }

    @PreAuthorize("hasAuthority('EDIT_INTERNAL_USER_ORG_BRANCH')")
    @PostMapping("set-org-branch/{internalId}")
    public EntityApiResponse<InternalUserView> setOrgBranch(
            @PathVariable(value = "internalId")
            @RequiredInternalUserStatus(
                    statuses = {UserStatus.PENDING_APPROVAL, UserStatus.ACTIVE},
                    message = "error.user.already.active"
            )
            String internalUserId,
            @RequestBody @Valid InternalUserOrgBranchUpdateForm form,
            Authentication auth,
            Locale locale){
        form.setSessionUserId(auth.getName());
        InternalUser user = internalUserEditService.setUserOrgBranch(form, internalUserId);
        return new EntityApiResponse<>(
            Message.get("user.branch.update.success", locale),
            new InternalUserView(user)
        );

    }

    @PreAuthorize("hasAuthority('EDIT_INTERNAL_USER_ORG_DEPARTMENT')")
    @PostMapping("update-org-department/{internalUserId}")
    public EntityApiResponse<InternalUserView> updateOrgDepartment(
            @PathVariable String internalUserId,
            @RequestBody InternalUserOrgDepartmentUpdateForm form,
            Authentication auth,
            Locale locale
    ){
        form.setSessionUserId(auth.getName());
        InternalUser user = internalUserEditService.setUserOrgDepartment(form, internalUserId);
        return new EntityApiResponse<>(
            Message.get("user.department.update.success", locale),
            new InternalUserView(user)
        );
    }

    @Autowired
    public void setInternalUserEditService(InternalUserEditService internalUserEditService) {
        this.internalUserEditService = internalUserEditService;
    }

    @Autowired
    public void setInternalUserReadService(InternalUserReadService internalUserReadService) {
        this.internalUserReadService = internalUserReadService;
    }

    @Autowired
    public void setInternalUserAuthGroupReadService(InternalUserAuthGroupReadService service){
        this.internalUserAuthGroupReadService = service;
    }
}
