package me.gustavwww.controller;

import me.gustavwww.services.TCPListener;
import me.gustavwww.services.TCPServer;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ServerController implements TCPListener {

    private final TCPServer tcpServer;

    private final List<ClientController> connections = Collections.synchronizedList(new ArrayList<>());

    public synchronized void removeConnection(ClientController controller) {
        connections.remove(controller);
    }

    public ServerController(int port) throws IOException {
        tcpServer = new TCPServer(port);
        tcpServer.addListener(this);

        new Thread(new SystemInputController(connections)).start();
    }

    public void startServer() throws IOException {
        tcpServer.listen();
    }

    @Override
    public void clientConnected(Socket client) {
        System.out.println("Client connected to server: " + client.getInetAddress().getHostAddress());
        ClientController controller = new ClientController(this, client);
        connections.add(controller);
        new Thread(controller).start();
    }
}
