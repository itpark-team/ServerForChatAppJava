package com.example.dao;

import com.example.daomodel.User;

import java.util.List;

public interface UsersDao {
    User authByLoginAndPassword(String login, String password) throws Exception;

    void registerNewUser(User user);

    boolean isContainLogin(String login);

    void setUserIsOnline(User user);

    void setUserIsOffline(User user);

    List<User> getAllUsersWithoutMe(User user) throws Exception;

    User getUserById(long id);


}
