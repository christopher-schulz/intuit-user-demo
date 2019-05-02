package com.intuitdemo.controller;

import com.intuitdemo.api.user.v1.UsersApi;
import com.intuitdemo.api.user.v1.model.Group;
import com.intuitdemo.api.user.v1.model.NewUser;
import com.intuitdemo.api.user.v1.model.User;
import com.intuitdemo.model.UserEntity;
import com.intuitdemo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
public class UserController implements UsersApi {
    @Autowired
    private UserService userService;

    @Autowired
    private ConversionService conversionService;

    @PreAuthorize("hasRole('USER_MANAGER')")
    @Override
    public ResponseEntity<User> createUser(@Valid NewUser newUser) {
        UserEntity newEntityUser = conversionService.convert(newUser, UserEntity.class);
        UserEntity result = userService.save(newEntityUser);
        return new ResponseEntity<>(conversionService.convert(result, User.class), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<User> getCurrentUser() {
        String username = Optional.ofNullable(SecurityContextHolder.getContext())
                .map(SecurityContext::getAuthentication)
                .map(Authentication::getPrincipal)
                .filter(String.class::isInstance)
                .map(String.class::cast)
                .orElseThrow(() -> new SecurityException("No current user"));

        UserEntity entityUser = userService.findByUsername(username);
        return new ResponseEntity<>(conversionService.convert(entityUser, User.class), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('USER_MANAGER')")
    @Override
    public ResponseEntity<List<User>> listUsers() {
        List<UserEntity> entityUsers = userService.findAll();
        List<User> userList = entityUsers.stream()
                .map(entityUser -> conversionService.convert(entityUser, User.class))
                .collect(Collectors.toList());

        return new ResponseEntity<>(userList, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('USER_MANAGER')")
    @Override
    public ResponseEntity<User> setGroups(Long userId, @Valid List<Group> group) {
        List<String> groupNameList = group.stream()
                .map(Group::getName)
                .collect(Collectors.toList());
        UserEntity user = userService.setGroups(userId, groupNameList);
        return new ResponseEntity<>(conversionService.convert(user, User.class), HttpStatus.OK);
    }
}
