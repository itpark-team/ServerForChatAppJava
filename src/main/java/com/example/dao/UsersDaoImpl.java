package com.example.dao;

import com.example.daomodel.User;
import lombok.AllArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

@AllArgsConstructor
public class UsersDaoImpl implements UsersDao {

    private SessionFactory sessionFactory;

    @Override
    public User authByLoginAndPassword(String login, String password) throws Exception {
        Query query = sessionFactory.openSession().createQuery("FROM User WHERE login = :login AND password = :password");
        query.setParameter("login", login);
        query.setParameter("password", password);

        List<User> users = (List<User>) query.getResultList();

        if (users == null) {
            throw new Exception(String.format("User with login=%s and password=%s not found", login, password));
        }

        return users.get(0);
    }

    @Override
    public User registerNewUser(User user) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        session.save(user);

        transaction.commit();
        session.close();

        return user;
    }

    @Override
    public boolean isContainLogin(String login) {
        Query query = sessionFactory.openSession().createQuery("FROM User WHERE login = :login");
        query.setParameter("login", login);

        List<User> users = (List<User>) query.getResultList();

        return users != null;
    }
}
