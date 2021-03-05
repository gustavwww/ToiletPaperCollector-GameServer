package me.gustavwww.controller.commandhandlers;

import me.gustavwww.controller.ClientController;
import me.gustavwww.services.protocol.Command;
import me.gustavwww.services.protocol.IServerProtocol;

public class IncreaseHandler extends AbstractCommandHandler {

    public IncreaseHandler() {
        super("count");
    }

    @Override
    protected void concreteHandle(Command cmd, ClientController client, IServerProtocol protocol) {
        // TODO: Add time checker "middleware" to check for hackers.
        client.increaseCount();
    }


}
