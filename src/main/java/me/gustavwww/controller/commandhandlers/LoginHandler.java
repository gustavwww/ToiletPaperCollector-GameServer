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

        if (args.length < 1) {
            client.sendTCP(protocol.writeError("Invalid arguments."));
            return;
        }

        String deviceID = args[0];
        String nickname = null;

        if (args.length >= 2) {
            nickname = args[1];
        }

        try {
            if (nickname == null) {
                client.login(deviceID);
            } else {
                client.signup(deviceID, nickname);
            }
        } catch (Exception e) {
            client.sendTCP(protocol.writeError(e.getMessage()));
        }

    }
}
