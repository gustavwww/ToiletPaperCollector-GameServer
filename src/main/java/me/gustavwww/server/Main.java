package me.gustavwww.server;

import java.io.IOException;

public class Main {

    public static void main(String[] args) {

        try {
            new GameServer(26000).listen();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
