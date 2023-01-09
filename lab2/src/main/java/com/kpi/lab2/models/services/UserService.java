package com.kpi.lab2.models.services;

import com.kpi.lab2.models.daos.UserDao;
import com.kpi.lab2.models.entities.User;

import java.util.List;

public class UserService extends EntityServiceBase<User, UserDao> {
    public UserService(UserDao userDao) {
        super(userDao);
    }

    public List<User> findByNameAndPassword(String name, String password) {
        return entityDao.findByNameAndPassword(name, password);
    }
}
