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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class UserController implements UsersApi {

    private final UserService userService;
    private final ConversionService conversionService;

    @Autowired
    public UserController(UserService userService, ConversionService conversionService) {
        this.userService = userService;
        this.conversionService = conversionService;
    }

    @Override
    public ResponseEntity<User> createUser(@Valid NewUser newUser) {
        UserEntity newEntityUser = conversionService.convert(newUser, UserEntity.class);
        UserEntity result = userService.save(newEntityUser);
        return new ResponseEntity<>(conversionService.convert(result, User.class), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<User> getCurrentUser() {
        String username = getUsername(SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        UserEntity entityUser = userService.findByUsername(username);
        return new ResponseEntity<>(conversionService.convert(entityUser, User.class), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<User>> listUsers() {
        List<UserEntity> entityUsers = userService.findAll();
        List<User> userList = entityUsers.stream()
                .map(entityUser -> conversionService.convert(entityUser, User.class))
                .collect(Collectors.toList());

        return new ResponseEntity<>(userList, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<User> setGroups(Long userId, @Valid List<Group> group) {
        List<String> groupNameList = group.stream()
                .map(Group::getName)
                .collect(Collectors.toList());
        UserEntity user = userService.setGroups(userId, groupNameList);
        return new ResponseEntity<>(conversionService.convert(user, User.class), HttpStatus.OK);
    }

    private String getUsername(Object principal) {
        if (principal instanceof String)
            return (String) principal;
        else if (principal instanceof org.springframework.security.core.userdetails.User)
            return ((org.springframework.security.core.userdetails.User)principal).getUsername();
        throw new SecurityException("No current user");
    }
}
