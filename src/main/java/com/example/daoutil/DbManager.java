package com.example.daoutil;

import com.example.dao.MessagesDao;
import com.example.dao.UsersDao;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DbManager {
    private UsersDao usersDao;
    private MessagesDao messagesDao;
}
