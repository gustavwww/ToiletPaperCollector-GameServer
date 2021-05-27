package me.gustavwww.model;

import me.gustavwww.db.HttpManager;
import me.gustavwww.db.HttpManagerException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

class User implements IUser {

    private String id;
    private String nickname;
    private int coins;
    private int amount;
    private int totalAmount;
    private int increment;

    User(String id, String nickname, int coins, int amount, int totalAmount) {
        this.id = id;
        this.nickname = nickname;
        this.coins = coins;
        this.amount = amount;
        this.totalAmount = totalAmount;
        increment = 0;
    }

    @Override
    public void increaseCount() {
        int inc = 1;
        if (getLevel() == 1) {
            inc = 3;
        }
        increment += inc;
    }

    private int getLevel() {
        if (amount >= 700) {
            return 1;
        }
        return 0;
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
    public int getCoins() {
        return coins;
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
    public void postUser() throws HttpManagerException, IOException, InterruptedException {

        Map<String, Object> body = new HashMap<>();
        body.put("id", id);
        body.put("nickname", nickname);
        body.put("increment", increment);

        HttpManager.sendPostRequest("/v1/users/", body);
    }


}
