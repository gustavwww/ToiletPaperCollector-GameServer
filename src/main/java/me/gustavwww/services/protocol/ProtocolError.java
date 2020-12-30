package me.gustavwww.services.protocol;

public enum ProtocolError {
    INVALID_ID("Invalid User ID"),
    INVALID_NICKNAME("Invalid Nickname");

    public final String msg;

    ProtocolError(String msg) {
        this.msg = msg;
    }

}
