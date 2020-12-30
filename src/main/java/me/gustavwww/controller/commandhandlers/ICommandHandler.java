package me.gustavwww.controller.commandhandlers;

import me.gustavwww.controller.ClientController;
import me.gustavwww.services.protocol.Command;
import me.gustavwww.services.protocol.IServerProtocol;

public interface ICommandHandler {

    void setNext(ICommandHandler handler);
    void handle(Command cmd, ClientController client, IServerProtocol protocol);

}
