package com.kpi.lab2.controllers;

import com.kpi.lab2.models.User;
import com.kpi.lab2.services.UserService;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class UserController {
    private UserService userService;

    public User createUser(User user) {
        return userService.create(user);
    }

    public void deleteUser(User user) {
        userService.delete(user);
    }
}
