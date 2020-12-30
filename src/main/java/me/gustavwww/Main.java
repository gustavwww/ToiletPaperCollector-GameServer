package me.gustavwww;

import me.gustavwww.controller.ServerController;

import java.io.IOException;

public class Main {

    public static void main(String[] args) {

        try {
            new ServerController(26000).startServer();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
