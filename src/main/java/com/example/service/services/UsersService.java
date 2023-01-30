package com.example.service.services;

import com.example.daomodel.User;
import com.example.daoutil.DbManager;
import com.example.dto.user.*;
import com.example.netengine.NetStatuses;
import com.example.netmodel.Response;
import com.example.netmodel.ServerException;
import com.google.gson.Gson;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.stream.Collectors;

public class UsersService {
    private DbManager dbManager;
    private Gson gson;
    private ModelMapper modelMapper;

    public UsersService(DbManager dbManager) {
        this.dbManager = dbManager;
        this.gson = new Gson();
        this.modelMapper = new ModelMapper();
    }

    public Response processAuthUser(String jsonData) throws Exception {
        AuthUserRequestDto requestUser = gson.fromJson(jsonData, AuthUserRequestDto.class);

        try {
            User outputUser = dbManager.getUsersDao().authByLoginAndPassword(requestUser.getLogin(), requestUser.getPassword());

            dbManager.getUsersDao().setUserIsOnline(outputUser);
            outputUser.setOnline(true);

//            AuthUserResponseDto responseUser = AuthUserResponseDto.builder()
//                    .id(outputUser.getId())
//                    .nickname(outputUser.getNickname())
//                    .build();

            AuthUserResponseDto responseUser = modelMapper.map(outputUser, AuthUserResponseDto.class);

            String outputUserJson = gson.toJson(responseUser);

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
        RegisterUserRequestDto requestUser = gson.fromJson(jsonData, RegisterUserRequestDto.class);

        try {
            boolean isContain = dbManager.getUsersDao().isContainLogin(requestUser.getLogin());

            if (isContain) {
                throw new Exception("Пользователь с таким логином уже существует");
            }

            User registerUser = modelMapper.map(requestUser, User.class);

            registerUser.setOnline(true);
            dbManager.getUsersDao().registerNewUser(registerUser);

            AuthUserResponseDto responseUser = modelMapper.map(registerUser, AuthUserResponseDto.class);

            String outputUserJson = gson.toJson(responseUser);

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

    public Response processDisconnectUser(String jsonData) throws Exception {
        DisconnectUserRequestDto requestUser = gson.fromJson(jsonData, DisconnectUserRequestDto.class);

        try {
            User disconnectUser = modelMapper.map(requestUser, User.class);
            dbManager.getUsersDao().setUserIsOffline(disconnectUser);

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

    public Response processGetAllUsersWithoutMe(String jsonData) {
        UsersWithoutMeRequestDto requestUser = gson.fromJson(jsonData, UsersWithoutMeRequestDto.class);

        try {
            User getUser = modelMapper.map(requestUser, User.class);
            List<User> users = dbManager.getUsersDao().getAllUsersWithoutMe(getUser);

            List<UsersWithoutMeResponseDto> responseUsers = users.stream().map(
                    user -> modelMapper.map(user, UsersWithoutMeResponseDto.class)
            ).collect(Collectors.toList());

            String outputUsersJson = gson.toJson(responseUsers);

            return Response.builder()
                    .status(NetStatuses.OK)
                    .jsonData(outputUsersJson)
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
