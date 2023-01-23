package com.example.service.services;

import com.example.daomodel.Message;
import com.example.daomodel.User;
import com.example.daoutil.DbManager;
import com.example.netengine.NetStatuses;
import com.example.netmodel.Response;
import com.example.netmodel.ServerException;
import com.google.gson.Gson;

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
}
