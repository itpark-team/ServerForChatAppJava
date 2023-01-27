package com.example;

import com.example.dao.MessagesDao;
import com.example.dao.MessagesDaoImpl;
import com.example.dao.UsersDao;
import com.example.dao.UsersDaoImpl;
import com.example.daomodel.Message;
import com.example.daomodel.User;
import com.example.daoutil.HibernateSession;
import com.example.netengine.ServerListener;
import org.hibernate.SessionFactory;

import java.util.List;

public class Main {
    public static void main(String[] args) throws Exception {
        ServerListener serverListener = new ServerListener(23256);
        serverListener.start();
    }
}
