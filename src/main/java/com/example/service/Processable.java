package com.example.service;

import com.example.netmodel.Response;

public interface Processable {
    Response process(String jsonData) throws Exception;
}
