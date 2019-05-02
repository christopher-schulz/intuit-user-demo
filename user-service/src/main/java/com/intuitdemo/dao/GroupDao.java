package com.intuitdemo.dao;

import com.intuitdemo.model.GroupEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface GroupDao extends CrudRepository<GroupEntity, Long> {
    Set<GroupEntity> findByNameIn(List<String> names);
}
