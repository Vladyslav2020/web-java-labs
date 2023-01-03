package com.kpi.lab2.models.services;

import com.kpi.lab2.models.daos.UserDao;
import com.kpi.lab2.models.entities.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import static org.mockito.ArgumentMatchers.any;

class UserServiceTest {
    public static List<User> users = List.of(
            User.builder()
                    .id(1L)
                    .isAdmin(true)
                    .name("admin1")
                    .password("pass1")
                    .build(),
            User.builder()
                    .id(2L)
                    .isAdmin(false)
                    .name("user2")
                    .password("pass2")
                    .build()
    );

    private UserService userService;

    @BeforeEach
    public void init() {
        UserDao userDao = Mockito.mock(UserDao.class);
        userService = new UserService(userDao);
        Mockito.doReturn(users).when(userDao).findAll();
        Mockito.doAnswer(invocation -> {
            Long id = invocation.getArgument(0, Long.class);
            return users.stream().filter(user -> user.getId().equals(id)).findFirst().orElseThrow(NoSuchElementException::new);
        }).when(userDao).findById(any(Long.class));

        Mockito.doAnswer(invocation -> {
            String username = invocation.getArgument(0, String.class);
            String password = invocation.getArgument(1, String.class);
            return users.stream().filter(user -> user.getName().equals(username) && user.getPassword().equals(password)).collect(Collectors.toList());
        }).when(userDao).findByNameAndPassword(any(String.class), any(String.class));
    }

    @Test
    public void findAll() {
        List<User> actualUsers = userService.findAll();
        Assertions.assertEquals(users.size(), actualUsers.size());
        Assertions.assertEquals(users, actualUsers);
    }

    @Test
    public void findByNameAndPassword() {
        List<User> actualUsers = userService.findByNameAndPassword("user2", "pass2");
        Assertions.assertEquals(1, actualUsers.size());
        Assertions.assertEquals(users.get(1), actualUsers.get(0));
    }

    @Test
    public void findById() {
        User actualUser = userService.findById(1L);
        Assertions.assertEquals(users.get(0), actualUser);
        Assertions.assertThrows(NoSuchElementException.class, () -> userService.findById(3L));
    }

}