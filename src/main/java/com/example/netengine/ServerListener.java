package com.example.netengine;

import com.example.service.Service;
import com.example.service.ServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.ServerSocket;
import java.net.Socket;

public class ServerListener {
    private final Logger logger = LoggerFactory.getLogger(ServerListener.class);

    private ServerSocket serverSocket;

    private Service service;

    public ServerListener(int port) throws Exception {
        serverSocket = new ServerSocket(port);
        service = new ServiceImpl();

        logger.info(String.format("server started on %s:%s", serverSocket.getInetAddress(), serverSocket.getLocalPort()));
    }

    public void start() throws Exception {
        while (true) {
            Socket socket = serverSocket.accept();
            logger.info(String.format("client connected from %s:%s", socket.getInetAddress(), socket.getPort()));

            ServerHandler serverHandler = new ServerHandler(socket, service);
            serverHandler.start();
        }
    }
}
