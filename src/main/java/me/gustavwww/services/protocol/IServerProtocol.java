package me.gustavwww.services.protocol;

public interface IServerProtocol {

    Command parseMessage(String msg);
    String writeMessage(Command cmd);

    String writeError(String msg);
}
