package me.gustavwww.model;

import me.gustavwww.db.HttpManagerException;

import java.io.IOException;

public interface IUser {
    String getId();
    String getUsername();
    int getCoins();
    int getAmount();
    int getTotalAmount();

    void increaseCount();
    void update() throws InterruptedException, IOException, HttpManagerException;
    void postIncrement() throws HttpManagerException, IOException, InterruptedException;
    void buySkin(String name) throws HttpManagerException, IOException, InterruptedException;
    void equipSkin(String name) throws HttpManagerException, IOException, InterruptedException;
}
