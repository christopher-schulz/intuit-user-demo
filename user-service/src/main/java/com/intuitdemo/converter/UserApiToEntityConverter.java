package com.intuitdemo.converter;

import com.intuitdemo.model.UserEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class UserApiToEntityConverter implements Converter<com.intuitdemo.api.user.v1.model.User, UserEntity> {
    private final Logger logger = LoggerFactory.getLogger(UserApiToEntityConverter.class);

    @Override
    public UserEntity convert(com.intuitdemo.api.user.v1.model.User user) {
        try {
            UserEntity entityUser = new UserEntity();
            entityUser.setUsername(user.getName());
            entityUser.setEmail(user.getEmail());
            return entityUser;
        } catch (Exception e) {
            logger.error("conversion exception=", e);
            throw new ConversionFailedException(TypeDescriptor.valueOf(UserEntity.class),
                    TypeDescriptor.valueOf(com.intuitdemo.api.user.v1.model.User.class), user, null);
        }
    }
}
