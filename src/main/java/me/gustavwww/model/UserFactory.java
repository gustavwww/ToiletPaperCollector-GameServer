package me.gustavwww.model;

import me.gustavwww.db.HttpManager;
import me.gustavwww.db.HttpManagerException;

import java.io.IOException;
import java.util.List;

public class UserFactory {

    public static IUser CreateUser(String id, String username, int coins, int amount, int totalAmount, List<String> skins, String equippedSkin) {
        return new User(id, username, coins, amount, totalAmount, skins, equippedSkin);
    }

    public static IUser CreateUserLogin(String username, String password) throws HttpManagerException, IOException, InterruptedException {
        return HttpManager.getUserLogin(username, password);
    }

    public static synchronized IUser CreateUserSignup(String username, String password) throws HttpManagerException, IOException, InterruptedException {
        return HttpManager.getUserSignup(username, password);
    }

}
