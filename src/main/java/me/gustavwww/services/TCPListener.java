package me.gustavwww.services;

import me.gustavwww.controller.ClientController;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPListener {

    private ServerSocket server;
    private int port;

    public TCPListener(int port) throws IOException {
        this.server = new ServerSocket(port);
        this.port = port;
    }


    public void listen() throws IOException {

        System.out.println("Server listening on port " + port);

        while(true) {
            Socket client = server.accept();
            System.out.println("New client connected: " + client.getInetAddress().getHostAddress());

            new Thread(new ClientController(client)).start();
        }


    }


}
