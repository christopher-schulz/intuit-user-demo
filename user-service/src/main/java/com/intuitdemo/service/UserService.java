package com.intuitdemo.service;

import com.intuitdemo.dao.GroupDao;
import com.intuitdemo.dao.UserDao;
import com.intuitdemo.model.GroupEntity;
import com.intuitdemo.model.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;


@Service
public class UserService {

	private final UserDao userDao;
	private final GroupDao groupDao;

	@Autowired
	public UserService(UserDao userDao, GroupDao groupDao) {
		this.userDao = userDao;
		this.groupDao = groupDao;
	}

	public List<UserEntity> findAll() {
		List<UserEntity> list = new ArrayList<>();
		userDao.findAll().iterator().forEachRemaining(list::add);
		return list;
	}

    public UserEntity save(UserEntity user) {
        return userDao.save(user);
    }

	public UserEntity findByUsername(String username) {
		return userDao.findByUsername(username);
	}

	public UserEntity setGroups(Long userId, List<String> groupNames) {
		UserEntity user = userDao.findById(userId)
				.orElseThrow(() -> new IllegalArgumentException(userId + " not found"));

		Set<GroupEntity> groups = groupDao.findByNameIn(groupNames);
		user.setGroups(groups);
		return userDao.save(user);
	}
}
