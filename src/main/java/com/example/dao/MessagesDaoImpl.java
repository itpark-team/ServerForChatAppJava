package com.example.dao;

import lombok.AllArgsConstructor;
import org.hibernate.SessionFactory;

@AllArgsConstructor
public class MessagesDaoImpl implements MessagesDao {

    private SessionFactory sessionFactory;
}
