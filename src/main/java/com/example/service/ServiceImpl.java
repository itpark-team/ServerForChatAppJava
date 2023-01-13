package com.example.service;

import com.example.netmodel.Request;
import com.example.netmodel.Response;

public class ServiceImpl implements Service{
    @Override
    public Response processRequest(Request request) throws Exception {
        return Response.builder().status(200).jsonData("abc").build();
    }
}
