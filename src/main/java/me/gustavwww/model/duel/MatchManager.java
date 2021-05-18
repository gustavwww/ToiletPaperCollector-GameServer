package me.gustavwww.model.duel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MatchManager {

    private static MatchManager instance = null;

    private final List<IMatchListener> listeners = Collections.synchronizedList(new ArrayList<>());

    public void addListener(IMatchListener listener) {
        listeners.add(listener);
    }

    public void removeListener(IMatchListener listener) {
        listeners.remove(listener);
    }

    public synchronized void dualRequest(IMatchListener sender, String nickname) {
        synchronized (listeners) {
            for (IMatchListener l : listeners) {
                if (l.getUser().getNickname().equals(nickname)) {
                    DuelRequest request = new DuelRequest(sender, l);
                    l.gotRequest(request);
                    sender.gotRequest(request);
                }
            }
        }
    }

    public static synchronized MatchManager getInstance() {
        if (instance == null) {
            instance = new MatchManager();
        }
        return instance;
    }
}
