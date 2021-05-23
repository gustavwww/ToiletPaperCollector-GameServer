package me.gustavwww.model.duel;

import me.gustavwww.model.IUser;

public interface IDuelListener {
    IUser getUser();
    void gameStarted();
    void gameEnded(IUser winner);
    void countTimerUpdated(String timerType, int counter);
    void userLeft(IUser user);
    void userReadyUp(IUser user);
    void userCount(IUser user, int count);
}
