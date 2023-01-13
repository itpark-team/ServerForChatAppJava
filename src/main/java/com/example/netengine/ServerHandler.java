package com.example.netengine;

import com.example.netmodel.Request;
import com.example.netmodel.Response;
import com.example.service.Service;
import com.google.gson.Gson;
import lombok.SneakyThrows;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

public class ServerHandler extends Thread {
    private Socket socket;

    private DataInputStream dataInputStream;
    private DataOutputStream dataOutputStream;

    private Service service;

    private Gson gson;

    public ServerHandler(Socket socket, Service service) throws Exception {
        this.socket = socket;
        this.service = service;

        this.dataInputStream = new DataInputStream(this.socket.getInputStream());
        this.dataOutputStream = new DataOutputStream(this.socket.getOutputStream());

        this.gson = new Gson();
    }

    @SneakyThrows
    @Override
    public void run() {
        while (true) {
            String requestAsJson = getRequest();
            Request request = fromJsonToRequest(requestAsJson);

            Response response = service.processRequest(request);

            String responseAsJson = fromResponseToJson(response);
            sendResponse(responseAsJson);
        }
    }

    private String getRequest() throws Exception {
        return dataInputStream.readUTF();
    }

    private void sendResponse(String responseAsJson) throws Exception {
        dataOutputStream.writeUTF(responseAsJson);
        dataOutputStream.flush();
    }

    private Request fromJsonToRequest(String requestAsJson) {
        return gson.fromJson(requestAsJson, Request.class);
    }

    private String fromResponseToJson(Response response) {
        return gson.toJson(response);
    }

}
