package com.example.dao;

import com.example.daomodel.Message;
import lombok.AllArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

@AllArgsConstructor
public class MessagesDaoImpl implements MessagesDao {

    private SessionFactory sessionFactory;


    @Override
    public void addNewMessage(Message message) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        session.save(message);

        transaction.commit();
        session.close();
    }
}
