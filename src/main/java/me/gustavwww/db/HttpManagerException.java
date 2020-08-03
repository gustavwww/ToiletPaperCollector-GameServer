package me.gustavwww.db;

public class HttpManagerException extends Exception {

    private final int statusCode;

    public HttpManagerException(String errorMsg, int statusCode) {
        super(errorMsg);
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return statusCode;
    }
}
