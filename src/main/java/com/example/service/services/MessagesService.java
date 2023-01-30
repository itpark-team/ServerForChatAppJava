package com.example.service.services;

import com.example.daomodel.Message;
import com.example.daomodel.User;
import com.example.daoutil.DbManager;
import com.example.dto.message.AddNewMessageRequestDto;
import com.example.dto.message.GetUncheckedMessagesRequestDto;
import com.example.dto.message.GetUncheckedMessagesResponseDto;
import com.example.dto.message.SetMessagesStatusIsOpenedRequestDto;
import com.example.netengine.NetStatuses;
import com.example.netmodel.Response;
import com.example.netmodel.ServerException;
import com.google.gson.Gson;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.stream.Collectors;

public class MessagesService {
    private DbManager dbManager;
    private Gson gson;
    private ModelMapper modelMapper;

    public MessagesService(DbManager dbManager) {
        this.dbManager = dbManager;
        this.gson = new Gson();
        this.modelMapper = new ModelMapper();
    }

    public Response processAddNewMessage(String jsonData) {
        AddNewMessageRequestDto requestMessage = gson.fromJson(jsonData, AddNewMessageRequestDto.class);

        try {

            Message message = Message.builder()
                    .text(requestMessage.getText())
                    .isOpened(false)
                    .fromUser(dbManager.getUsersDao().getUserById(requestMessage.getFromUserId()))
                    .toUser(dbManager.getUsersDao().getUserById(requestMessage.getToUserId()))
                    .build();

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

        GetUncheckedMessagesRequestDto requestUser = gson.fromJson(jsonData, GetUncheckedMessagesRequestDto.class);

        try {
            User user = modelMapper.map(requestUser, User.class);

            List<Message> uncheckedMessages = dbManager.getMessagesDao().getUncheckedMessages(user);

            List<GetUncheckedMessagesResponseDto> responseMessages = uncheckedMessages.stream().map(
                    message -> GetUncheckedMessagesResponseDto.builder()
                            .fromUserNickname(message.getFromUser().getNickname())
                            .text(message.getText())
                            .build()
            ).collect(Collectors.toList());

            //todo как сделать конвертацию json при LAZY загрузке доп сущностей https://stackoverflow.com/questions/13459718/could-not-serialize-object-cause-of-hibernateproxy java.lang.UnsupportedOperationException: Attempted to serialize java.lang.Class: org.hibernate.proxy.HibernateProxy. Forgot to register a type adapter?
            String outputMessagesJson = gson.toJson(responseMessages);

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

    public Response processSetMessagesStatusIsOpened(String jsonData) {

        SetMessagesStatusIsOpenedRequestDto requestUser = gson.fromJson(jsonData, SetMessagesStatusIsOpenedRequestDto.class);

        try {
            User user = modelMapper.map(requestUser, User.class);

            dbManager.getMessagesDao().setMessagesStatusIsOpened(user);

            return Response.builder()
                    .status(NetStatuses.OK)
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
}
