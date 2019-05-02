package com.intuitdemo.dao;

import com.intuitdemo.model.AuthEventEntity;
import com.intuitdemo.model.UserEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthEventDao extends CrudRepository<AuthEventEntity, Long> {
    UserEntity findByUsername(String username);
}
