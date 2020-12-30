package me.gustavwww.services;

import java.io.IOException;

public class Main {

    public static void main(String[] args) {

        try {
            new TCPListener(26000).listen();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
