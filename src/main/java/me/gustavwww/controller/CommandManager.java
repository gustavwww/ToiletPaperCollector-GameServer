package me.gustavwww.controller;

import me.gustavwww.controller.commandhandlers.ICommandHandler;
import me.gustavwww.controller.commandhandlers.IncreaseHandler;
import me.gustavwww.controller.commandhandlers.LoginHandler;
import me.gustavwww.controller.commandhandlers.SetFacebookHandler;
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
        ICommandHandler loginHandler = new LoginHandler();
        ICommandHandler increaseHandler = new IncreaseHandler();
        ICommandHandler setFacebookHandler = new SetFacebookHandler();

        loginHandler.setNext(increaseHandler);
        increaseHandler.setNext(setFacebookHandler);

        firstHandler = loginHandler;
    }

    synchronized void handleCommand(Command cmd) {
        firstHandler.handle(cmd, client, protocol);
    }

}
