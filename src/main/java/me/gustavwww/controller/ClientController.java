package me.gustavwww.controller;

import me.gustavwww.db.HttpManagerException;
import me.gustavwww.db.HttpStatusCode;
import me.gustavwww.model.IUser;
import me.gustavwww.model.UserFactory;
import me.gustavwww.services.protocol.Command;
import me.gustavwww.services.protocol.IServerProtocol;
import me.gustavwww.services.protocol.ServerProtocolFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientController implements Runnable {

    private final Socket client;
    private BufferedReader reader;
    private PrintWriter writer;

    private final IServerProtocol protocol;

    private IUser user;
    private int increment = 0;

    public ClientController(Socket client) {
        this.client = client;
        protocol = ServerProtocolFactory.getServerProtocol();
    }

    public void increaseCount(int amount) {
        increment += amount;
    }

    public void login(String id, String nickname) throws IOException, InterruptedException {

        try {
            user = UserFactory.CreateUser(id);

        } catch (HttpManagerException e) {
            if (e.getStatusCode() == HttpStatusCode.NOT_FOUND.code) {

                if (nickname == null) {
                    sendTCP(protocol.writeError(e.getMessage()));
                }

                user = UserFactory.CreateUser(id, nickname, 0);
                postUser();
            }
        }

    }

    private void postUser() throws IOException, InterruptedException {
        if (user == null) { return; }

        try {
            user.postUser(increment);
        } catch (HttpManagerException e) {
            sendTCP(protocol.writeError(e.getMessage()));
        }

    }

    private void disconnect() {

        try {
            System.out.println("Client disconnected from server: " + client.getInetAddress().getHostAddress());
            postUser();
            client.close();

        } catch (Exception ignored) {}

        Thread.currentThread().interrupt();
    }

    @Override
    public void run() {

        try {

            this.reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
            this.writer = new PrintWriter(client.getOutputStream(), true);

            CommandManager cmdManager = new CommandManager(this);

            String input;
            while((input = reader.readLine()) != null) {

                Command cmd = protocol.parseMessage(input);
                cmdManager.handleCommand(cmd);

            }

            disconnect();

        } catch (Exception e) {
            disconnect();
        }


    }

    public void sendTCP(String msg) {
        writer.println(msg);
    }


}
