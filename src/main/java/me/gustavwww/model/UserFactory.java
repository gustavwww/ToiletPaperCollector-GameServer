package me.gustavwww.model;

import me.gustavwww.db.HttpManager;
import me.gustavwww.db.HttpManagerException;

import java.io.IOException;

public class UserFactory {

    public static IUser CreateUser(String id, String nickname, int coins, int amount, int totalAmount) {
        return new User(id, nickname, coins, amount, totalAmount);
    }

    public static IUser CreateUser(String id) throws HttpManagerException, IOException, InterruptedException {
        return HttpManager.getUser(id);
    }

}
