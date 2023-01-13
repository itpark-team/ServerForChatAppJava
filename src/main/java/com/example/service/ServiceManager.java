package com.example.service;

import com.example.netmodel.Request;
import com.example.netmodel.Response;

public interface ServiceManager {
    Response processRequest(Request request) throws Exception;
}
