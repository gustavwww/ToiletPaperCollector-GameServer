package me.gustavwww.controller;

import me.gustavwww.db.HttpManager;
import me.gustavwww.db.HttpManagerException;
import me.gustavwww.model.*;
import me.gustavwww.model.duel.ClientDuelHandler;
import me.gustavwww.model.duel.DuelController;
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

    private final ServerController serverController;

    private final Socket client;
    private BufferedReader reader;
    private PrintWriter writer;

    private final IServerProtocol protocol;

    private ClientDuelHandler duelHandler = null;
    private IUser user = null;

    public ClientController(ServerController serverController, Socket client) {
        this.serverController = serverController;
        this.client = client;
        protocol = ServerProtocolFactory.getServerProtocol();
    }

    public void increaseCount() {
        if (user == null) { return; }
        user.increaseCount();
    }

    public void login(String username, String password) throws IOException, InterruptedException {
        try {
            this.user = UserFactory.CreateUserLogin(username, password);
            sendTCP("logged:" + user.getUsername() + "," + user.getCoins() + "," + user.getTotalAmount() + "," + user.getAmount());
            setupDuelHandler(user);
        } catch (HttpManagerException e) {
            sendTCP(protocol.writeError(e.getMessage()));
        }
    }

    public void signup(String username, String password) throws IOException, InterruptedException {
        try {
            this.user = UserFactory.CreateUserSignup(username, password);
            sendTCP("logged:" + user.getUsername() + "," + user.getCoins() + "," + user.getTotalAmount() + "," + user.getAmount());
            setupDuelHandler(user);
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
            user.postIncrement();
        } catch (HttpManagerException e) {
            sendTCP(protocol.writeError(e.getMessage()));
        }
    }

    private void setupDuelHandler(IUser user) {
        duelHandler = new ClientDuelHandler(this, user);
        duelHandler.allowRequests();
    }

    public synchronized void disconnect() {

        try {
            System.out.println("Client disconnected from server: " + client.getInetAddress().getHostAddress());
            if (duelHandler != null) {
                duelHandler.denyRequests();
                duelHandler.getDuelController().leaveDuel();
            }
            postUser();
            client.close();
            serverController.removeConnection(this);

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

    public void sendDuelRequest(String nickname) {
        if (duelHandler == null) { return; }
        duelHandler.sendRequest(nickname);
    }

    public void acceptDuelRequest() {
        if (duelHandler == null) { return; }
        duelHandler.acceptRequest();
    }

    public void declineDuelRequest() {
        if (duelHandler == null) { return; }
        duelHandler.declineRequest();
    }

    public DuelController getDuelController() {
        return duelHandler.getDuelController();
    }

}
