package me.gustavwww.services;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class TCPServer {

    private ServerSocket server;
    private int port;

    private List<TCPListener> listeners = new ArrayList<>();

    public void addListener(TCPListener listener) {
        listeners.add(listener);
    }

    public void removeListener(TCPListener listener) {
        listeners.remove(listener);
    }

    public TCPServer(int port) throws IOException {
        this.server = new ServerSocket(port);
        this.port = port;
    }

    public void listen() throws IOException {

        System.out.println("Server listening on port " + port);

        while(true) {
            Socket client = server.accept();
            notifyClientConnected(client);
        }

    }

    private void notifyClientConnected(Socket client) {
        for (TCPListener l : listeners) {
            l.clientConnected(client);
        }
    }


}
