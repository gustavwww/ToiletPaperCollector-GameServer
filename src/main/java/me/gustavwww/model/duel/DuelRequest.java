package me.gustavwww.model.duel;

import me.gustavwww.services.timer.CountdownTimer;

import java.util.*;

public class DuelRequest {

    private static final int TIMEOUT = 5;

    private final IMatchListener sender;
    private final Map<IMatchListener, Boolean> status = Collections.synchronizedMap(new HashMap<>());

    private Timer timer;

    public DuelRequest(IMatchListener sender, IMatchListener user2) {
        this.sender = sender;

        status.put(sender, true);
        status.put(user2, null);

        startTimer();
    }

    private void startTimer() {
        timer = new Timer();
        TimerTask task = new CountdownTimer(TIMEOUT, () -> {
            cancelRequest();
            timer.cancel();
        });

        timer.schedule(task, 0, 1000);
    }

    private void controlAcceptance() {
        synchronized (status) {
            for (Boolean accepted : status.values()) {
                if (!accepted) {
                    return;
                }
            }
        }
        finishRequest();
    }

    private void cancelRequest() {
        timer.cancel();
        synchronized (status) {
            for (IMatchListener l : status.keySet()) {
                l.requestCancelled();
            }
        }
    }

    private void finishRequest() {
        timer.cancel();
        Duel duel = new Duel(2);
        new Thread(duel).start();
        synchronized (status) {
            for (IMatchListener l : status.keySet()) {
                l.requestFinished(duel);
            }
        }
    }

    public synchronized void acceptRequest(IMatchListener user) {
        synchronized (status) {
            if (status.containsKey(user)) {
                status.put(user, true);
            }
        }
        controlAcceptance();
    }

    public synchronized void declineRequest(IMatchListener user) {
        synchronized (status) {
            if (status.containsKey(user)) {
                status.put(user, false);
            }
        }
    }

    public IMatchListener getSender() {
        return sender;
    }


}
