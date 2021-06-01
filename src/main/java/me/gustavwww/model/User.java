package me.gustavwww.model;

import me.gustavwww.db.HttpManager;
import me.gustavwww.db.HttpManagerException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class User implements IUser {

    private final String id;
    private final String username;
    private int coins;
    private int amount;
    private int totalAmount;

    private List<String> skins;
    private String equippedSkin;

    private int increment;

    User(String id, String username, int coins, int amount, int totalAmount, List<String> skins, String equippedSkin) {
        this.id = id;
        this.username = username;
        this.coins = coins;
        this.amount = amount;
        this.totalAmount = totalAmount;

        this.skins = skins;
        this.equippedSkin = equippedSkin;

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

    public void update() throws InterruptedException, IOException, HttpManagerException {

        Map<String, Object> response = HttpManager.sendGetRequest("/v1/users/" + id);

        coins = (int) (double) response.get("coins");
        amount = (int) (double) response.get("weeklyAmount");
        totalAmount = (int) (double) response.get("amount");

        Map<String, Object> skinObj = (Map<String, Object>) response.get("skins");
        skins = (ArrayList<String>) skinObj.get("owned");
        equippedSkin = (String) skinObj.get("equippedSkin");
    }

    @Override
    public void postIncrement() throws HttpManagerException, IOException, InterruptedException {

        Map<String, Object> body = new HashMap<>();
        body.put("increment", increment);

        HttpManager.sendPostRequest("/v1/users/" + id, body);
    }


}
