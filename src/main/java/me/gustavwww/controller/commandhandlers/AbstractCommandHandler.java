package me.gustavwww.controller.commandhandlers;

import me.gustavwww.controller.ClientController;
import me.gustavwww.services.protocol.Command;
import me.gustavwww.services.protocol.IServerProtocol;

abstract class AbstractCommandHandler implements ICommandHandler {

    private String command;
    private ICommandHandler next;

    protected AbstractCommandHandler(String command) {
        this.command = command;
    }

    @Override
    public void setNext(ICommandHandler handler) {
        next = handler;
    }

    @Override
    public void handle(Command cmd, ClientController client, IServerProtocol protocol) {
        if (!cmd.getCmd().equalsIgnoreCase(command)) {
            if (next != null) {
                next.handle(cmd, client, protocol);
            }
            return;
        }

        concreteHandle(cmd, client, protocol);
    }

    protected abstract void concreteHandle(Command cmd, ClientController client, IServerProtocol protocol);

}
