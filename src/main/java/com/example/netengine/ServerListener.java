package com.example.netengine;

import com.example.service.ServiceManager;
import com.example.service.ServiceManagerImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.ServerSocket;
import java.net.Socket;

public class ServerListener {
    private final Logger logger = LoggerFactory.getLogger(ServerListener.class);

    private ServerSocket serverSocket;

    private ServiceManager serviceManager;

    public ServerListener(int port) throws Exception {
        serverSocket = new ServerSocket(port);
        serviceManager = new ServiceManagerImpl();

        logger.info(String.format("server started on %s:%s", serverSocket.getInetAddress(), serverSocket.getLocalPort()));
    }

    public void start() throws Exception {
        while (true) {
            Socket clientSocket = serverSocket.accept();
            logger.info(String.format("client connected from %s:%s", clientSocket.getInetAddress(), clientSocket.getPort()));

            ServerHandler serverHandler = new ServerHandler(clientSocket, serviceManager);
            serverHandler.start();
        }
    }
}
