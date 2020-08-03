package me.gustavwww.model;

import me.gustavwww.db.HttpManager;
import me.gustavwww.db.HttpManagerException;
import me.gustavwww.db.HttpStatusCode;

import java.io.IOException;

class User implements IUser {

    private String id;
    private String nickname;
    private int amount;

    User(String id, String nickname, int amount) {
        this.id = id;
        this.nickname = nickname;
        this.amount = amount;
    }

    public String getId() {
        return id;
    }

    public String getNickname() {
        return nickname;
    }

    public int getAmount() {
        return amount;
    }

    public void postUser(int increment) throws HttpManagerException, IOException, InterruptedException {
        HttpManager.postUserPaper(this, increment);
    }


}
