package com.intuitdemo.converter;

import com.intuitdemo.api.user.v1.model.Group;
import com.intuitdemo.api.user.v1.model.User;
import com.intuitdemo.model.GroupEntity;
import com.intuitdemo.model.UserEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class UserEntityToApiConverter implements Converter<UserEntity, User> {
    private final Logger logger = LoggerFactory.getLogger(UserEntityToApiConverter.class);

    @Override
    public User convert(UserEntity user) {
        try {
            User apiUser = new User();
            apiUser.setId(user.getId());
            apiUser.setEmail(user.getEmail());
            apiUser.setName(user.getUsername());
            apiUser.setGroups(Optional.ofNullable(user.getGroups())
                .map(groups -> groups.stream()
                    .map(UserEntityToApiConverter::groupConvert)
                    .collect(Collectors.toList()))
            .orElse(new ArrayList<>()));
            return apiUser;
        } catch (Exception e) {
            logger.error("conversion exception=", e);
            throw new ConversionFailedException(TypeDescriptor.valueOf(UserEntity.class),
                    TypeDescriptor.valueOf(User.class), user, null);
        }
    }

    private static Group groupConvert(GroupEntity groupEntity) {
        Group group = new Group();
        group.setName(groupEntity.getName());
        return group;
    }
}
