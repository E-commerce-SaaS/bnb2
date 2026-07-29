package io.internaluser.service;

import io.internaluser.entity.InternalUser;
import io.internaluser.repository.InternalUserRepository;
import io.internaluser.view.InternalUserAuthView;
import io.user.form.FetchUserForm;
import io.user.service.BaseUserReadService;
import io.userpermission.model.AuthGroup;
import io.userpermission.model.UserPermission;
import io.userpermission.service.AuthGroupPermissionReadService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InternalUserReadService extends BaseUserReadService<InternalUser, InternalUserRepository> {
    private AuthGroupPermissionReadService authGroupPermissionReadService;
    private InternalUserAuthGroupReadService internalUserAuthGroupReadService;

    public InternalUserAuthView getInternalUserAuthView(InternalUser user){
        InternalUserAuthView view = new InternalUserAuthView(user);
        List<AuthGroup> groups = internalUserAuthGroupReadService.findAuthGroupsByInternalUser(user);
        List<UserPermission> perms = authGroupPermissionReadService.findPermissionsByAuthGroups(groups);
        view.setUserPermissions(perms);
        view.setAuthGroups(groups);
        return view;
    }

    public Page<InternalUser> listUsers(FetchUserForm form) {
        Specification<InternalUser> spec = repository.notDeleted()
                .and(repository.createAtBetween(form.getStartDate(), form.getEndDate()));

        if (StringUtils.isNotBlank(form.getQuery())) {
            spec = spec.and(
                repository.nameLike(form.getQuery())
                    .or(repository.emailLike(form.getQuery()))
                    .or(repository.phoneNumberLike(form.getQuery()))
            );
        }

        if (form.getUserStatus() != null) {
            spec = spec.and(repository.userStatusIs(form.getUserStatus()));
        }

        return repository.findAll(spec, repository.defaultPageable(form));
    }

    @Autowired
    public void setInternalUserAuthGroupReadService(AuthGroupPermissionReadService service){
        this.authGroupPermissionReadService = service;
    }

    @Autowired
    public void setInternalUserAuthGroupReadService(InternalUserAuthGroupReadService service){
        this.internalUserAuthGroupReadService = service;
    }
}
