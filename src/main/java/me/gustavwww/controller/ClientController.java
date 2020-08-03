package me.gustavwww.controller;

import me.gustavwww.db.HttpManagerException;
import me.gustavwww.db.HttpStatusCode;
import me.gustavwww.model.IUser;
import me.gustavwww.model.UserFactory;
import me.gustavwww.server.ProtocolError;
import me.gustavwww.server.ProtocolException;
import me.gustavwww.server.ServerProtocol;

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

    private void initUser() {

    }

    private String readId() {

        try {
            System.out.println("Waiting for user id...");
            String input = reader.readLine();

            return ServerProtocol.parseId(input);

        } catch (ProtocolException e) {
            writer.println(ServerProtocol.writeError(e.getProtocolError()));

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private String readNickname() {

        try {
            System.out.println("Waiting for user nickname...");
            String input = reader.readLine();

            return ServerProtocol.parseNickname(input);

        } catch (ProtocolException e) {
            writer.println(ServerProtocol.writeError(e.getProtocolError()));
            readNickname();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private IUser getUser(String id) {

        try {

            return UserFactory.CreateUser(id);

        } catch (HttpManagerException e) {
            if (e.getStatusCode() == HttpStatusCode.NOT_FOUND.code) {
                // User not found, create one.
                String nick = readNickname();
                if (nick != null) {
                    return UserFactory.CreateUser(id, nick, 0);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    private void postUser(IUser user, int increment) {

        try {

            user.postUser(increment);

        } catch (HttpManagerException e) {
            writer.println(ServerProtocol.writeError(ProtocolError.INVALID_NICKNAME));

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void run() {

        try {

            this.reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
            this.writer = new PrintWriter(client.getOutputStream(), true);

            initUser();

            String input;

            while((input = reader.readLine()) != null) {

                if (ServerProtocol.parseCount(input)) {
                    increment++;
                }

            }

            client.close();

        } catch (IOException e) {
            e.printStackTrace();
        }


    }


}
