package me.gustavwww.controller.commandhandlers;

import me.gustavwww.controller.ClientController;
import me.gustavwww.services.protocol.Command;
import me.gustavwww.services.protocol.IServerProtocol;

public class IncreaseHandler extends AbstractCommandHandler {

    private static final int HIGH_SEND_RATE = 5000;

    long lastCountTime = 0;

    public IncreaseHandler() {
        super("count");
    }

    @Override
    protected void concreteHandle(Command cmd, ClientController client, IServerProtocol protocol) {
        if (System.currentTimeMillis() - lastCountTime < HIGH_SEND_RATE) {
            return;
        }

        lastCountTime = System.currentTimeMillis();
        client.increaseCount();
    }


}
