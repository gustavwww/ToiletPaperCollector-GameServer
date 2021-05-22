package me.gustavwww.model.duel;

import me.gustavwww.services.timer.CountdownTimer;

import java.util.*;

public class Duel {

    private static final int COUNTDOWN = 3;
    private static final int GAME_LENGTH = 15;

    private final int clientCount;
    private final Map<IDuelListener, Integer> clients = Collections.synchronizedMap(new HashMap<>());

    private boolean running = false;
    private boolean gameover = false;
    private boolean canClick = false;

    public Duel(int clientCount) {
        this.clientCount = clientCount;
    }

    public void joinDuel(IDuelListener c) {
        if (clients.size() >= clientCount || gameover || running) { return; }
        clients.put(c, null);
    }

    public synchronized void readyUp(IDuelListener client) {
        if (gameover || running) { return; }
        clients.put(client, 0);
        informReadyUp(client);
        checkAllReady();
    }

    public synchronized void sendCount(IDuelListener client) {
        if (gameover || !canClick) { return; }
        clients.put(client, clients.get(client) + 1);
        informCount(client);
    }

    private void checkAllReady() {
        if (clients.size() < clientCount) {
            return;
        }

        synchronized (clients) {
            for (IDuelListener c : clients.keySet()) {
                if (clients.get(c) == null) {
                    return;
                }
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

    private void informReadyUp(IDuelListener client) {
        synchronized (clients) {
            for (IDuelListener c : clients.keySet()) {
                c.userReadyUp(client.getUser());
            }
        }
    }

    private void informCount(IDuelListener client) {
        synchronized (clients) {
            for (IDuelListener c : clients.keySet()) {
                c.userCount(client.getUser(), clients.get(client));
            }
        }
    }


}
