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
import java.io.IOException;
import java.net.Socket;

public class ServerHandler extends Thread {
    private final Logger logger = LoggerFactory.getLogger(ServerHandler.class);

    private Socket clientSocket;

    private DataInputStream dataInputStream;
    private DataOutputStream dataOutputStream;

    private ServiceManager serviceManager;

    private Gson gson;

    public ServerHandler(Socket clientSocket, ServiceManager serviceManager) throws Exception {
        this.clientSocket = clientSocket;
        this.serviceManager = serviceManager;

        this.dataInputStream = new DataInputStream(this.clientSocket.getInputStream());
        this.dataOutputStream = new DataOutputStream(this.clientSocket.getOutputStream());

        this.gson = new Gson();
    }

    @SneakyThrows
    @Override
    public void run() {
        boolean isChatting = true;
        while (isChatting) {
            String requestAsJson = getRequest();
            Request request = fromJsonToRequest(requestAsJson);

            logger.info(String.format("from client %s:%s get request: %s", clientSocket.getInetAddress(), clientSocket.getPort(), request));

            Response response = serviceManager.processRequest(request);

            String responseAsJson = fromResponseToJson(response);
            sendResponse(responseAsJson);

            logger.info(String.format("to client %s:%s send response: %s", clientSocket.getInetAddress(), clientSocket.getPort(), response));

            if (request.getCommand().equals(NetCommands.DISCONNECT_USER)) {
                isChatting = false;
            }
        }

        logger.info(String.format("client %s:%s disconnected", clientSocket.getInetAddress(), clientSocket.getPort()));

        closeConnection();
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

    private void closeConnection() throws Exception {
        dataInputStream.close();
        dataOutputStream.close();

        clientSocket.close();
    }

}
