package me.gustavwww.controller;

import me.gustavwww.db.HttpManager;
import me.gustavwww.db.HttpManagerException;
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
import java.util.HashMap;
import java.util.Map;

public class ClientController implements Runnable {

    private final Socket client;
    private BufferedReader reader;
    private PrintWriter writer;

    private final IServerProtocol protocol;

    private IUser user = null;

    public ClientController(Socket client) {
        this.client = client;
        protocol = ServerProtocolFactory.getServerProtocol();
    }

    public void increaseCount() {
        if (user == null) { return; }
        user.increaseCount();
    }

    public void login(String id) throws IOException, InterruptedException {

        try {
            this.user = UserFactory.CreateUser(id);
            sendTCP("logged:" + user.getNickname() + "," + user.getTotalAmount() + "," + user.getAmount());
        } catch (HttpManagerException e) {
            sendTCP(protocol.writeError(e.getMessage()));
        }
    }

    public void signup(String id, String nickname) throws IOException, InterruptedException {
        IUser user = UserFactory.CreateUser(id, nickname, 0, 0);

        try {
            user.postUser();
            this.user = user;
            sendTCP("logged:" + user.getNickname() + "," + user.getTotalAmount() + "," + user.getAmount());
        } catch (HttpManagerException e) {
            sendTCP(protocol.writeError(e.getMessage()));
        }

    }

    public void sendFacebookDetails(String f_id, String f_name) throws IOException, InterruptedException {
        if (user == null) { return; }

        try {
            Map<String, Object> body = new HashMap<>();
            body.put("id", user.getId());
            body.put("f_id", f_id);
            body.put("f_name", f_name);

            HttpManager.sendPostRequest("/v1/users/setfb/", body);
        } catch (HttpManagerException e) {
            sendTCP(protocol.writeError(e.getMessage()));
        }

    }

    private void postUser() throws IOException, InterruptedException {
        if (user == null) { return; }

        try {
            user.postUser();
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

            sendTCP("connected");

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

    public synchronized void sendTCP(String msg) {
        writer.println(msg);
    }


}
