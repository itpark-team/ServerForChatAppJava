package com.example.service;

import com.example.dao.MessagesDao;
import com.example.dao.MessagesDaoImpl;
import com.example.dao.UsersDao;
import com.example.dao.UsersDaoImpl;
import com.example.daoutil.DbManager;
import com.example.daoutil.HibernateSession;
import com.example.netmodel.Request;
import com.example.netmodel.Response;
import com.example.service.services.MessagesService;
import com.example.service.services.UsersService;
import org.hibernate.SessionFactory;

import java.util.HashMap;
import java.util.Map;


public class ServiceManagerImpl implements ServiceManager {

    private Map<String, Processable> methods;
    private DbManager dbManager;

    public ServiceManagerImpl() {
        SessionFactory sessionFactory = new HibernateSession().getSessionFactory();

        UsersDao usersDao = new UsersDaoImpl(sessionFactory);
        MessagesDao messagesDao = new MessagesDaoImpl(sessionFactory);

        dbManager = new DbManager(usersDao, messagesDao);

        UsersService usersService = new UsersService(dbManager);
        MessagesService messagesService = new MessagesService(dbManager);

        methods = new HashMap<>();

        methods.put("empty", messagesService::processEmpty);
    }

    @Override
    public Response processRequest(Request request) throws Exception {
        return methods.get(request.getCommand()).process(request.getJsonData());


    }
}