package com.example.dao;

import com.example.daomodel.User;
import lombok.AllArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.jboss.logging.Messages;

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

        if (users.size() == 0) {
            throw new Exception(String.format("Пользователь с логином=%s и паролем=%s не найден", login, password));
        }

        return users.get(0);
    }

    @Override
    public List<User> getAllUsersWithoutMe(User user) throws Exception {
        Query query = sessionFactory.openSession().createQuery("FROM User WHERE id<>:id ORDER BY id");
        query.setParameter("id", user.getId());

        List<User> users = (List<User>) query.getResultList();

        return users;
    }

    @Override
    public User getUserById(long id) {
        return sessionFactory.openSession().get(User.class, id);
    }


    @Override
    public void registerNewUser(User user) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        session.save(user);

        transaction.commit();
        session.close();
    }

    @Override
    public boolean isContainLogin(String login) {
        Query query = sessionFactory.openSession().createQuery("FROM User WHERE login = :login");
        query.setParameter("login", login);

        List<User> users = (List<User>) query.getResultList();

        return users.size() != 0;
    }

    @Override
    public void setUserIsOnline(User user) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        Query query = session.createQuery("update User set isOnline = :isOnline where id = :id");

        query.setParameter("isOnline", true);
        query.setParameter("id", user.getId());

        query.executeUpdate();

        transaction.commit();
        session.close();
    }

    @Override
    public void setUserIsOffline(User user) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        Query query = session.createQuery("update User set isOnline = :isOnline where id = :id");

        query.setParameter("isOnline", false);
        query.setParameter("id", user.getId());

        query.executeUpdate();

        transaction.commit();
        session.close();
    }
}
