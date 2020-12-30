package me.gustavwww.controller;

import me.gustavwww.controller.commandhandlers.ICommandHandler;
import me.gustavwww.services.protocol.Command;
import me.gustavwww.services.protocol.IServerProtocol;
import me.gustavwww.services.protocol.ServerProtocolFactory;

class CommandManager {

    private final ClientController client;
    private final IServerProtocol protocol;

    private ICommandHandler firstHandler;

    CommandManager(ClientController client) {
        this.client = client;
        protocol = ServerProtocolFactory.getServerProtocol();
        setupHandlers();
    }

    private void setupHandlers() {

    }

    synchronized void handleCommand(Command cmd) {
        firstHandler.handle(cmd, client, protocol);
    }

}
