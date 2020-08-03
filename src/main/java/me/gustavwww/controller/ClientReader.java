package me.gustavwww.controller;

import me.gustavwww.server.ProtocolException;
import me.gustavwww.server.ServerProtocol;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

public class ClientReader {

    private BufferedReader reader;
    private PrintWriter feedBack;

    public ClientReader(BufferedReader reader, PrintWriter feedBack) {
        this.reader = reader;
        this.feedBack = feedBack;
    }

    public String readId() throws IOException {

        while (true) {

            try {
                feedBack.println(ServerProtocol.writeWant("id"));
                System.out.println("Waiting for user id...");
                String input = reader.readLine();
                System.out.println(input);
                return ServerProtocol.parseId(input);

            } catch (ProtocolException e) {
                feedBack.println(e.getProtocolError().msg);
                readId();

            }

        }

    }

    public String readNickname() throws IOException {

        while (true) {

            try {
                feedBack.println(ServerProtocol.writeWant("nickname"));
                System.out.println("Waiting for user nickname...");
                String input = reader.readLine();

                return ServerProtocol.parseNickname(input);

            } catch (ProtocolException e) {
                feedBack.println(e.getProtocolError().msg);
            }

        }

    }


}
