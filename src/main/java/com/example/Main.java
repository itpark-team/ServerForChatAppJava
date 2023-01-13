package com.example;

import com.example.netengine.ServerListener;

public class Main {
    public static void main(String[] args) throws Exception {
        ServerListener serverListener = new ServerListener(23256);
        serverListener.start();
    }
}
