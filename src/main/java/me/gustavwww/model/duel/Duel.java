package me.gustavwww.model.duel;

import me.gustavwww.services.timer.CountdownTimer;

import java.util.*;

public class Duel implements Runnable {

    private static final int COUNTDOWN = 3;
    private static final int GAME_LENGTH = 15;

    private final int clientCount;
    private final Map<IDuelListener, Integer> clients = Collections.synchronizedMap(new HashMap<>());

    private final Queue<DuelAction> actionQueue = new LinkedList<>();

    private boolean canClick = false;
    private boolean running = false;
    private boolean gameover = false;


    public Duel(int clientCount) {
        this.clientCount = clientCount;
    }

    public synchronized void addAction(DuelAction action) {
        actionQueue.add(action);
    }

    private void doAction() {
        if (!actionQueue.isEmpty()) {
            actionQueue.remove().doAction(this);
        }
    }

    public void joinDuel(IDuelListener c) {
        if (clients.size() >= clientCount || gameover || running) { return; }
        clients.put(c, null);
    }

    public void leaveDuel(IDuelListener c) {
        if (clients.containsKey(c)) {
            informLeft(c);
        }
    }

    public void readyUp(IDuelListener client) {
        if (gameover || running || !clients.containsKey(client)) { return; }
        clients.put(client, 0);
        informReadyUp(client);
        checkAllReady();
    }

    public void sendCount(IDuelListener client) {
        if (gameover || !canClick) { return; }
        clients.put(client, clients.get(client) + 1);
        informCount(client);
    }

    @Override
    public void run() {

        while(!gameover) {
            doAction();
        }

    }

    private void checkAllReady() {
        if (clients.size() < clientCount) {
            return;
        }

        for (IDuelListener c : clients.keySet()) {
            if (clients.get(c) == null) {
                return;
            }
        }

        startGame();
    }

    private void startGame() {

        running = true;

        Timer startTimer = new Timer();
        TimerTask task = new CountdownTimer(COUNTDOWN, () -> {

            startTimer.cancel();
            canClick = true;
            createGameTimer();
            informStarted();

        }, (counter) -> {

            informTimerUpdate("starttimer", counter);
        });

        startTimer.schedule(task, 0, 1000);
    }

    private void createGameTimer() {

        Timer gameTimer = new Timer();
        TimerTask task = new CountdownTimer(GAME_LENGTH, () -> {

            gameTimer.cancel();
            endGame();

        }, (counter) -> {

            informTimerUpdate("gametimer", counter);
        });

        gameTimer.schedule(task, 0, 1000);
    }

    private void endGame() {
        running = false;
        canClick = false;
        gameover = true;

        IDuelListener highest = null;
        for (IDuelListener c : clients.keySet()) {
            if (highest == null || clients.get(c) > clients.get(highest)) {
                highest = c;
            }
        }

        informEnded(highest);
    }

    private void informTimerUpdate(String timerType, int counter) {
        synchronized (clients) {
            for (IDuelListener c : clients.keySet()) {
                c.countTimerUpdated(timerType, counter);
            }
        }
    }

    private void informStarted() {
        synchronized (clients) {
            for (IDuelListener c : clients.keySet()) {
                c.gameStarted();
            }
        }
    }

    private void informEnded(IDuelListener winner) {
        synchronized (clients) {
            for (IDuelListener c : clients.keySet()) {
                c.gameEnded(winner.getUser());
            }
        }
    }

    private void informLeft(IDuelListener client) {
        for (IDuelListener c : clients.keySet()) {
            c.userLeft(client.getUser());
        }
    }

    private void informReadyUp(IDuelListener client) {
        for (IDuelListener c : clients.keySet()) {
            c.userReadyUp(client.getUser());
        }
    }

    private void informCount(IDuelListener client) {
        for (IDuelListener c : clients.keySet()) {
            c.userCount(client.getUser(), clients.get(client));
        }
    }

}
