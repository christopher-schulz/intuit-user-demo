package com.intuitdemo.service;

import com.intuitdemo.dao.UserDao;
import com.intuitdemo.model.GroupEntity;
import com.intuitdemo.model.UserEntity;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.Matchers.startsWith;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class UserServiceTest {
    @Mock
    private UserDao userDao;

    private String TEST_USER = "testUserName";

    @Test(expected = UsernameNotFoundException.class)
    public void testLoadByUsernameNotFound() {
        // GIVEN
        UserService service = new UserService(userDao);

        // WHEN
        service.loadUserByUsername(TEST_USER);

        // THEN
        // BOOM
    }

    @Test
    public void testLoadByUsernameNoGroups() {
        // GIVEN
        UserService service = new UserService(userDao);
        UserEntity entity = new UserEntity();
        entity.setUsername(TEST_USER);
        entity.setPassword("foo");
        when(userDao.findByUsername(eq(TEST_USER))).thenReturn(entity);

        // WHEN
        UserDetails result = service.loadUserByUsername(TEST_USER);

        // THEN
        assertThat(result.getUsername(), is(TEST_USER));
    }

    @Test
    public void testLoadByUsernameMultipleGroups() {
        // GIVEN
        UserService service = new UserService(userDao);
        UserEntity entity = new UserEntity();
        entity.setUsername(TEST_USER);
        entity.setPassword("foo");
        GroupEntity group1 = new GroupEntity();
        group1.setName("blah");
        group1.setId(34124L);
        GroupEntity group2 = new GroupEntity();
        group2.setName("blerg");
        group2.setId(311114124L);
        Set<GroupEntity> groups = new HashSet<>();
        groups.add(group1);
        groups.add(group2);
        entity.setGroups(groups);
        when(userDao.findByUsername(eq(TEST_USER))).thenReturn(entity);

        // WHEN
        UserDetails result = service.loadUserByUsername(TEST_USER);

        // THEN
        assertThat(result.getUsername(), is(TEST_USER));
        Collection<? extends GrantedAuthority> authorities = result.getAuthorities();
        assertThat(authorities.size(), is(2));
        authorities.forEach(authority ->
                assertThat(authority.getAuthority(), startsWith("ROLE_bl")
                ));
    }

}
