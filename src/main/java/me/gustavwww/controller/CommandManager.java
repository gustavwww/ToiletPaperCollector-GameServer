package me.gustavwww.controller;

import me.gustavwww.controller.commandhandlers.*;
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
        ICommandHandler signupHandler = new SignupHandler();
        ICommandHandler increaseHandler = new IncreaseHandler();
        ICommandHandler duelHandler = new DuelHandler();
        ICommandHandler skinHandler = new SkinHandler();

        loginHandler.setNext(signupHandler);
        signupHandler.setNext(increaseHandler);
        increaseHandler.setNext(duelHandler);
        duelHandler.setNext(skinHandler);

        firstHandler = loginHandler;
    }

    synchronized void handleCommand(Command cmd) {
        firstHandler.handle(cmd, client, protocol);
    }

}
