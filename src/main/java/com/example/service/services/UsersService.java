package com.example.service.services;

import com.example.daomodel.User;
import com.example.daoutil.DbManager;
import com.example.netengine.NetStatuses;
import com.example.netmodel.Response;
import com.example.netmodel.ServerException;
import com.google.gson.Gson;

public class UsersService {
    private DbManager dbManager;
    private Gson gson;

    public UsersService(DbManager dbManager) {
        this.dbManager = dbManager;
        this.gson = new Gson();
    }

    public Response processAuthUser(String jsonData) throws Exception {
        User inputUser = gson.fromJson(jsonData, User.class);

        try {
            User outputUser = dbManager.getUsersDao().authByLoginAndPassword(inputUser.getLogin(), inputUser.getPassword());

            dbManager.getUsersDao().setUserIsOnline(outputUser);
            outputUser.setOnline(true);

            String outputUserJson = gson.toJson(outputUser);

            return Response.builder()
                    .status(NetStatuses.OK)
                    .jsonData(outputUserJson)
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

    public Response processRegisterUser(String jsonData) throws Exception {
        User user = gson.fromJson(jsonData, User.class);

        try {
            boolean isContain = dbManager.getUsersDao().isContainLogin(user.getLogin());

            if (isContain) {
                throw new Exception("Пользователь с таким логином уже существует");
            }

            user.setOnline(true);
            dbManager.getUsersDao().registerNewUser(user);

            String outputUserJson = gson.toJson(user);

            return Response.builder()
                    .status(NetStatuses.OK)
                    .jsonData(outputUserJson)
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
