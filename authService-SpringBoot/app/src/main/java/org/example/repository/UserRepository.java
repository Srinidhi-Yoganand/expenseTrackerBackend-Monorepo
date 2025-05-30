package org.example.repository;

import org.example.entities.UserInfo;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<UserInfo, String> {

    UserInfo findByUsername(String username);

    UserInfo findByUserId(String userId);
}
