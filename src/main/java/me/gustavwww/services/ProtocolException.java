package me.gustavwww.services;

public class ProtocolException extends Exception {

    private final ProtocolError protocolError;

    public ProtocolException(ProtocolError protocolError) {
        super(protocolError.msg);
        this.protocolError = protocolError;
    }

    public ProtocolError getProtocolError() {
        return protocolError;
    }
}
