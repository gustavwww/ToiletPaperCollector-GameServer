package me.gustavwww.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class SystemInputController implements Runnable {

    private final List<ClientController> connections;

    SystemInputController(List<ClientController> connections) {
        this.connections = connections;
    }

    @Override
    public void run() {
        Scanner s = new Scanner(System.in);

        while (true) {
            System.out.print("Command: ");
            String in = s.nextLine();
            if (in.equalsIgnoreCase("stop")) {
                System.out.println("Disconnecting and saving user information...");
                disconnectClients();
                System.out.println("Terminating...");
                System.exit(0);
            }
        }
    }

    private void disconnectClients() {
        List<ClientController> copyClients = new ArrayList<>(connections);
            for (ClientController client : copyClients) {
                client.disconnect();
            }
    }


}
