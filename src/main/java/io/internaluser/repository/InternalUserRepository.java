package io.internaluser.repository;


import io.internaluser.entity.InternalUser;
import io.user.repository.UserRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InternalUserRepository extends UserRepository<InternalUser> {

}
