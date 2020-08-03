package me.gustavwww.model;

import me.gustavwww.db.HttpManagerException;

import java.io.IOException;

public interface IUser {
    String getId();
    String getNickname();
    int getAmount();

    void postUser(int increment) throws HttpManagerException, IOException, InterruptedException;
}
