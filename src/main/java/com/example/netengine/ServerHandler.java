package com.example.netengine;

import com.example.netmodel.Request;
import com.example.netmodel.Response;
import com.example.service.ServiceManager;
import com.google.gson.Gson;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

public class ServerHandler extends Thread {
    private final Logger logger = LoggerFactory.getLogger(ServerHandler.class);

    private Socket socket;

    private DataInputStream dataInputStream;
    private DataOutputStream dataOutputStream;

    private ServiceManager serviceManager;

    private Gson gson;

    public ServerHandler(Socket socket, ServiceManager serviceManager) throws Exception {
        this.socket = socket;
        this.serviceManager = serviceManager;

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

            logger.info(String.format("from client %s:%s get request: %s", socket.getInetAddress(), socket.getPort(), request));

            Response response = serviceManager.processRequest(request);

            String responseAsJson = fromResponseToJson(response);
            sendResponse(responseAsJson);

            logger.info(String.format("to client %s:%s send response: %s", socket.getInetAddress(), socket.getPort(), response));
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
