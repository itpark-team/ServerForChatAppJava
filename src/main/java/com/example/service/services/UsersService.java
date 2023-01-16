package com.example.service.services;

import com.example.daomodel.User;
import com.example.daoutil.DbManager;
import com.example.netengine.NetStatuses;
import com.example.netmodel.Response;
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
            return Response.builder()
                    .status(NetStatuses.USER_NOT_FOUND)
                    .build();
        }
    }
}
