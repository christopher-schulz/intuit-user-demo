package com.intuitdemo.controller;

import com.intuitdemo.api.user.v1.model.NewUser;
import com.intuitdemo.api.user.v1.model.User;
import com.intuitdemo.converter.GroupApiToGroupEntityConverter;
import com.intuitdemo.converter.NewUserApiToEntityConverter;
import com.intuitdemo.converter.UserApiToEntityConverter;
import com.intuitdemo.converter.UserEntityToApiConverter;
import com.intuitdemo.model.UserEntity;
import com.intuitdemo.service.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.context.support.ConversionServiceFactoryBean;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class UserControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private BCryptPasswordEncoder encoder;

    private ConversionService conversionService = null;

    private static final String TEST_USER = "TESTUSERNAME";

    @Before
    public void init() {
        ConversionServiceFactoryBean bean = new ConversionServiceFactoryBean();
        Set<Converter<?, ?>> converterSet = new HashSet<>();
        converterSet.add(new GroupApiToGroupEntityConverter());
        converterSet.add(new UserEntityToApiConverter());
        converterSet.add(new NewUserApiToEntityConverter(encoder));
        converterSet.add(new UserApiToEntityConverter());
        bean.setConverters(converterSet); //add converters
        bean.afterPropertiesSet();
        conversionService = bean.getObject();

        when(encoder.encode(any())).thenReturn("Super secret");
    }

    @Test
    public void testCreateUser() {
        // GIVEN
        UserController controller = new UserController(userService, conversionService);
        NewUser newUser = new NewUser()
                .email("test@test.org")
                .name(TEST_USER)
                .password("foo");
        UserEntity userEntity = new UserEntity();
        userEntity.setId(215);
        userEntity.setUsername(newUser.getName());
        userEntity.setPassword(newUser.getPassword());
        when(userService.save(any())).thenReturn(userEntity);

        // WHEN
        ResponseEntity<User> result = controller.createUser(newUser);

        // THEN
        assertThat(result.getStatusCode(), is(HttpStatus.OK));
        User resultUser = result.getBody();
        assertThat(resultUser.getName(), is(TEST_USER));
        assertThat(resultUser.getId(), is(userEntity.getId()));
    }

    @Test
    @WithMockUser("bob")
    public void testGetCurrentUser() {
        // GIVEN
        UserController controller = new UserController(userService, conversionService);
        UserEntity userEntity = new UserEntity();
        userEntity.setId(217);
        userEntity.setUsername("bob");
        userEntity.setPassword("sssssss");
        when(userService.findByUsername(eq(userEntity.getUsername()))).thenReturn(userEntity);


        // WHEN
        ResponseEntity<User> result = controller.getCurrentUser();

        // THEN
        assertThat(result.getStatusCode(), is(HttpStatus.OK));
        User resultUser = result.getBody();
        assertThat(resultUser.getName(), is(userEntity.getUsername()));
        assertThat(resultUser.getId(), is(userEntity.getId()));
    }
}
