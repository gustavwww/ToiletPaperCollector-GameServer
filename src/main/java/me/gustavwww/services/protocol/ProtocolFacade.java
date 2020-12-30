package me.gustavwww.services.protocol;

import java.util.Arrays;

class ProtocolFacade implements IServerProtocol {

    private static ProtocolFacade instance = null;

    static synchronized ProtocolFacade getInstance() {
        if (instance == null) {
            instance = new ProtocolFacade();
        }
        return instance;
    }

    private final ProtocolReader protocolReader;
    private final ProtocolWriter protocolWriter;

    private ProtocolFacade() {
        this.protocolReader = new ProtocolReader();
        this.protocolWriter = new ProtocolWriter();
    }


    @Override
    public synchronized Command parseMessage(String msg) {
        return protocolReader.parseMessage(msg);
    }

    @Override
    public synchronized String writeMessage(Command cmd) {
        return protocolWriter.writeMessage(cmd);
    }

    @Override
    public synchronized String writeError(String msg) {
        return "error:" + msg;
    }

}
