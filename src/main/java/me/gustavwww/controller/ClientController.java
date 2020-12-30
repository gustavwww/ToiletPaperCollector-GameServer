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

    private Socket client;
    private BufferedReader reader;
    private PrintWriter writer;

    private IUser user;
    private int increment = 0;

    public ClientController(Socket client) {
        this.client = client;
    }

    private void initUser() throws IOException, InterruptedException {

        String id = clientReader.readId();

        this.user = getUser(id);
        if (user == null) {
            disconnect();
            return;
        }

        postUser(user, 0);
    }

    private void disconnect() {

        try {
            System.out.println("Client disconnected from server: " + client.getInetAddress().getHostAddress());
            postUser(user, increment);
            client.close();

        } catch (Exception ignored) {}

        Thread.currentThread().interrupt();
    }

    private IUser getUser(String id) throws IOException, InterruptedException {

        try {
            return UserFactory.CreateUser(id);

        } catch (HttpManagerException e) {
            if (e.getStatusCode() == HttpStatusCode.NOT_FOUND.code) {
                // User not found, create one.
                String nickname = clientReader.readNickname();
                return UserFactory.CreateUser(id, nickname, 0);
            }
        }

        return null;
    }

    private void postUser(IUser user, int increment) throws IOException, InterruptedException {

        IUser postingUser = user;
        while (true) {

            try {
                if (postingUser == null) { return; }

                postingUser.postUser(increment);
                return;

            } catch (HttpManagerException e) {
                writer.println(ServerProtocol.writeError(ProtocolError.INVALID_NICKNAME));
                String nickname = clientReader.readNickname();
                postingUser = UserFactory.CreateUser(user.getId(), nickname, user.getAmount());
            }

        }

    }

    @Override
    public void run() {

        try {

            this.reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
            this.writer = new PrintWriter(client.getOutputStream(), true);

            IServerProtocol serverProtocol = ServerProtocolFactory.getServerProtocol();
            CommandManager cmdManager = new CommandManager(this);

            String input;
            while((input = reader.readLine()) != null) {

                Command cmd = serverProtocol.parseMessage(input);
                cmdManager.handleCommand(cmd);

            }

            disconnect();

        } catch (Exception e) {
            disconnect();
        }


    }


}
