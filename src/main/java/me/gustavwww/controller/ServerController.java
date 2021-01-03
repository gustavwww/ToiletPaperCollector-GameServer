package me.gustavwww.controller;

import me.gustavwww.services.TCPListener;
import me.gustavwww.services.TCPServer;

import java.io.IOException;
import java.net.Socket;

public class ServerController implements TCPListener {

    private final TCPServer tcpServer;

    public ServerController(int port) throws IOException {
        tcpServer = new TCPServer(port);
        tcpServer.addListener(this);
    }

    public void startServer() throws IOException {
        tcpServer.listen();
    }

    @Override
    public void clientConnected(Socket client) {
        System.out.println("Client connected to server: " + client.getInetAddress().getHostAddress());
        new Thread(new ClientController(client)).start();
    }
}
