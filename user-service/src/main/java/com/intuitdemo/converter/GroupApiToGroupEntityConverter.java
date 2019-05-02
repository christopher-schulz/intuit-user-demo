package com.intuitdemo.converter;

import com.intuitdemo.api.user.v1.model.Group;
import com.intuitdemo.api.user.v1.model.NewUser;
import com.intuitdemo.model.GroupEntity;
import com.intuitdemo.model.UserEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class GroupApiToGroupEntityConverter implements Converter<Group, GroupEntity> {
    private final Logger logger = LoggerFactory.getLogger(GroupApiToGroupEntityConverter.class);

    @Override
    public GroupEntity convert(Group group) {
        try {
            GroupEntity groupEntity = new GroupEntity();
            groupEntity.setName(group.getName());
            return groupEntity;
        } catch (Exception e) {
            logger.error("conversion exception=", e);
            throw new ConversionFailedException(TypeDescriptor.valueOf(UserEntity.class),
                    TypeDescriptor.valueOf(NewUser.class), group, null);
        }
    }
}
