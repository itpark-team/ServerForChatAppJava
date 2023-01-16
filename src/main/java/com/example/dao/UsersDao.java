package com.example.dao;

import com.example.daomodel.User;

public interface UsersDao {
    User authByLoginAndPassword(String login, String password) throws Exception;

    User registerNewUser(User user);

    boolean isContainLogin(String login);

    void setUserIsOnline(User user);

    void setUserIsOffline(User user);

}
