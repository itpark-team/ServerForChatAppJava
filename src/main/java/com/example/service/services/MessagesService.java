package com.example.service.services;

import com.example.daomodel.Message;
import com.example.daomodel.User;
import com.example.daoutil.DbManager;
import com.example.netengine.NetStatuses;
import com.example.netmodel.Response;
import com.example.netmodel.ServerException;
import com.google.gson.Gson;

import java.util.List;

public class MessagesService {
    private DbManager dbManager;
    private Gson gson;

    public MessagesService(DbManager dbManager) {
        this.dbManager = dbManager;
        this.gson = new Gson();
    }

    public Response processAddNewMessage(String jsonData) {
        Message message = gson.fromJson(jsonData, Message.class);

        try {

            long fromUserId = message.getFromUser().getId();
            long toUserId = message.getToUser().getId();

            message.setFromUser(
                    dbManager.getUsersDao().getUserById(fromUserId)
            );


            message.setToUser(
                    dbManager.getUsersDao().getUserById(toUserId)
            );


            message.setOpened(false);

            dbManager.getMessagesDao().addNewMessage(message);

            return Response.builder()
                    .status(NetStatuses.OK)
                    .build();

        } catch (Exception e) {
            ServerException exception = new ServerException(e.getMessage());

            String exceptionJson = gson.toJson(exception);

            return Response.builder()
                    .status(NetStatuses.BAD_REQUEST)
                    .jsonData(exceptionJson)
                    .build();
        }
    }

    public Response processGetUncheckedMessages(String jsonData) {

        User user = gson.fromJson(jsonData, User.class);

        try {
            List<Message> uncheckedMessages = dbManager.getMessagesDao().getUncheckedMessages(user);

            String outputMessagesJson = gson.toJson(uncheckedMessages);

            return Response.builder()
                    .status(NetStatuses.OK)
                    .jsonData(outputMessagesJson)
                    .build();

        } catch (Exception e) {

            e.printStackTrace();

            ServerException exception = new ServerException(e.getMessage());

            String exceptionJson = gson.toJson(exception);

            return Response.builder()
                    .status(NetStatuses.BAD_REQUEST)
                    .jsonData(exceptionJson)
                    .build();
        }
    }

    public Response processSetMessagesStatusIsOpened(String jsonData){

        User user = gson.fromJson(jsonData, User.class);

        try{

            dbManager.getMessagesDao().setMessagesStatusIsOpened(user);

            return Response.builder()
                    .status(NetStatuses.OK)
                    .build();
        }catch (Exception e){

            e.printStackTrace();

            ServerException exception = new ServerException(e.getMessage());

            String exceptionJson = gson.toJson(exception);

            return Response.builder()
                    .status(NetStatuses.BAD_REQUEST)
                    .jsonData(exceptionJson)
                    .build();
        }
    }
}
