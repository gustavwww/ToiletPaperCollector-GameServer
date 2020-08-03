package me.gustavwww.db;

public enum HttpStatusCode {

    NOT_FOUND(404);

    public final int code;

    HttpStatusCode(int code) {
        this.code = code;
    }

}
