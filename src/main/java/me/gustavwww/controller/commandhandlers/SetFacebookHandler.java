package me.gustavwww.controller.commandhandlers;

import me.gustavwww.controller.ClientController;
import me.gustavwww.services.protocol.Command;
import me.gustavwww.services.protocol.IServerProtocol;

public class SetFacebookHandler extends AbstractCommandHandler {

    public SetFacebookHandler() {
        super("setfb");
    }


    @Override
    protected void concreteHandle(Command cmd, ClientController client, IServerProtocol protocol) {
        if (cmd.getArgs().length < 2) {
            client.sendTCP(protocol.writeError("Invalid arguments."));
            return;
        }

        String f_id = cmd.getArgs()[0];
        String f_name = cmd.getArgs()[1];

        try {
            client.sendFacebookDetails(f_id, f_name);
        } catch (Exception e) {
            client.sendTCP(protocol.writeError(e.getMessage()));
        }
    }

}
