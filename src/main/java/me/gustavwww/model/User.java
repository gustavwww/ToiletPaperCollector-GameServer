package me.gustavwww.model;

import me.gustavwww.db.HttpManager;
import me.gustavwww.db.HttpManagerException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

class User implements IUser {

    private String id;
    private String nickname;
    private int amount;
    private int totalAmount;
    private int increment;

    User(String id, String nickname, int amount, int totalAmount) {
        this.id = id;
        this.nickname = nickname;
        this.amount = amount;
        this.totalAmount = totalAmount;
        increment = 0;
    }

    @Override
    public void increaseCount() {
        increment += 2*(calculateLevel()-1) + 1;
    }

    private int calculateLevel() {
        // y = 150(x-1)^2 + 100(x-1) + 1000
        // x = -1/3 + sqrt(1/9 - (1000-y)/150) + 1
        return (int) Math.abs(-1.0/3 + Math.sqrt(1.0/9 - (1000.0-amount)/150)) + 1;
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
    public void postUser() throws HttpManagerException, IOException, InterruptedException {

        Map<String, Object> body = new HashMap<>();
        body.put("id", id);
        body.put("nickname", nickname);
        body.put("increment", increment);

        HttpManager.sendPostRequest("/v1/users/", body);
    }


}
