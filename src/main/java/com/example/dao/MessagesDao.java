package com.example.dao;

import com.example.daomodel.Message;
import com.example.daomodel.User;

import java.util.List;

public interface MessagesDao {
    void addNewMessage(Message message);
    List<Message> getUncheckedMessages(User user);
    void setMessagesStatusIsOpened(User user);
}
