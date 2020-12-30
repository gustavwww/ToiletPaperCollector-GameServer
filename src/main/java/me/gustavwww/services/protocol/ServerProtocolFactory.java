package me.gustavwww.services.protocol;

public class ServerProtocolFactory {

    public static IServerProtocol getServerProtocol() {
        return ProtocolFacade.getInstance();
    }

}
