package com.example.netengine;

import com.example.service.Service;
import com.example.service.ServiceImpl;

import java.net.ServerSocket;
import java.net.Socket;

public class ServerListener {
    private ServerSocket serverSocket;

    private Service service;

    public ServerListener(int port) throws Exception {
        serverSocket = new ServerSocket(port);
        service = new ServiceImpl();
    }

    public void start() throws Exception {
        while (true) {
            Socket socket = serverSocket.accept();

            ServerHandler serverHandler = new ServerHandler(socket, service);
            serverHandler.start();
        }
    }
}
