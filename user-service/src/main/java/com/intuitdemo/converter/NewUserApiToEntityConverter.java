package com.intuitdemo.converter;

import com.intuitdemo.api.user.v1.model.NewUser;
import com.intuitdemo.model.UserEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class NewUserApiToEntityConverter implements Converter<NewUser, UserEntity> {
    private final Logger logger = LoggerFactory.getLogger(NewUserApiToEntityConverter.class);

    @Autowired
    private BCryptPasswordEncoder encoder;

    @Override
    public UserEntity convert(NewUser user) {
        try {
            UserEntity entityUser = new UserEntity();
            entityUser.setUsername(user.getName());
            entityUser.setEmail(user.getEmail());
            entityUser.setPassword(encoder.encode(user.getPassword()));
            return entityUser;
        } catch (Exception e) {
            logger.error("conversion exception=", e);
            throw new ConversionFailedException(TypeDescriptor.valueOf(UserEntity.class),
                    TypeDescriptor.valueOf(NewUser.class), user, null);
        }
    }
}
