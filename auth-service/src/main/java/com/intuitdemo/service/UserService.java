package com.intuitdemo.service;

import com.intuitdemo.dao.UserDao;
import com.intuitdemo.model.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class UserService implements UserDetailsService {
	
	@Autowired
	private UserDao userDao;

	public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
		UserEntity user = userDao.findByUsername(userId);
		if(user == null)
			throw new UsernameNotFoundException("Invalid username or password.");

		List<SimpleGrantedAuthority> authorities = Optional.ofNullable(user.getGroups())
				.map(groups ->
						groups.stream().map(group ->
								new SimpleGrantedAuthority("ROLE_" + group.getName())).collect(Collectors.toList()))
				.orElse(new ArrayList<>());

		return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), authorities);
	}
}
