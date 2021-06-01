package me.gustavwww.model;

import me.gustavwww.db.HttpManager;
import me.gustavwww.db.HttpManagerException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

class User implements IUser {

    private final String id;
    private final String username;
    private final int coins;
    private final int amount;
    private final int totalAmount;
    private int increment;

    User(String id, String username, int coins, int amount, int totalAmount) {
        this.id = id;
        this.username = username;
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
    public String getUsername() {
        return username;
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
    public void postIncrement() throws HttpManagerException, IOException, InterruptedException {

        Map<String, Object> body = new HashMap<>();
        body.put("increment", increment);

        HttpManager.sendPostRequest("/v1/users/" + id, body);
    }


}
