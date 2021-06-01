package me.gustavwww.controller.commandhandlers;

import me.gustavwww.controller.ClientController;
import me.gustavwww.services.protocol.Command;
import me.gustavwww.services.protocol.IServerProtocol;

public class LoginHandler extends AbstractCommandHandler {

    public LoginHandler() {
        super("login");
    }

    @Override
    protected void concreteHandle(Command cmd, ClientController client, IServerProtocol protocol) {
        String[] args = cmd.getArgs();

        if (args.length < 2) {
            client.sendTCP(protocol.writeError("Invalid arguments."));
            return;
        }

        String username = args[0];
        String password = args[1];

        try {
            client.login(username, password);
        } catch (Exception e) {
            client.sendTCP(protocol.writeError(e.getMessage()));
        }

    }
}
