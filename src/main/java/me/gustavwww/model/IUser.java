package me.gustavwww.model;

import me.gustavwww.db.HttpManagerException;

import java.io.IOException;

public interface IUser {
    String getId();
    String getNickname();
    int getAmount();
    int getTotalAmount();

    void increaseCount();
    void postUser() throws HttpManagerException, IOException, InterruptedException;
}
