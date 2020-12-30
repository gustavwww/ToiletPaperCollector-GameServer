package me.gustavwww.services;

import java.net.Socket;

public interface TCPListener {

    void clientConnected(Socket client);
}
