package me.gustavwww.model.duel;

import me.gustavwww.model.IUser;

public interface IMatchListener {
    IUser getUser();
    void gotRequest(DuelRequest request);
    void requestNotFound(String nickname);
    void requestCancelled();
    void requestFinished(Duel duel);
}
