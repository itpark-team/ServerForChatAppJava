package com.example.service.services;

import com.example.daoutil.DbManager;
import com.example.netmodel.Response;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class MessagesService {
    private DbManager dbManager;

    public Response processEmpty(String jsonData) throws Exception {
        return Response.builder().status(200).jsonData(jsonData+"!!!!!").build();
    }
}
