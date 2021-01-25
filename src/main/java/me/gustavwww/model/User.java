package me.gustavwww.model;

import me.gustavwww.db.HttpManager;
import me.gustavwww.db.HttpManagerException;

import java.io.IOException;

class User implements IUser {

    private String id;
    private String nickname;
    private int amount;
    private int totalAmount;

    User(String id, String nickname, int amount, int totalAmount) {
        this.id = id;
        this.nickname = nickname;
        this.amount = amount;
        this.totalAmount = totalAmount;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getNickname() {
        return nickname;
    }

    @Override
    public int getAmount() {
        return amount;
    }

    @Override
    public int getTotalAmount() {
        return totalAmount;
    }

    @Override
    public void postUser(int increment) throws HttpManagerException, IOException, InterruptedException {
        HttpManager.postUserPaper(this, increment);
    }


}
