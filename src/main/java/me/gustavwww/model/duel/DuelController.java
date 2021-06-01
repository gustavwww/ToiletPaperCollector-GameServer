package me.gustavwww.model.duel;

import me.gustavwww.controller.ClientController;
import me.gustavwww.model.IUser;

public class DuelController implements IDuelListener {

    private Duel duel = null;

    private final IUser user;
    private final ClientController clientController;

    public DuelController(IUser user, ClientController clientController) {
        this.user = user;
        this.clientController = clientController;
    }

    public void joinDuel(Duel duel) {
        this.duel = duel;
        this.duel.addAction(d -> {
            d.joinDuel(this);
        });
    }

    public void leaveDuel() {
        if (duel == null) { return; }
        duel.addAction(d -> {
            d.leaveDuel(this);
        });
    }

    public void readyUp() {
        if (duel == null) { return; }
        duel.addAction(d -> {
            d.readyUp(this);
        });
    }

    public void sendCount() {
        if (duel == null) { return; }
        duel.addAction(d -> {
            d.sendCount(this);
        });
    }

    @Override
    public void gameStarted() {
        clientController.sendTCP("duel:started");
    }

    @Override
    public void gameEnded(IUser winner) {
        this.duel = null;
        clientController.sendTCP("duel:ended," + winner.getUsername());
    }

    @Override
    public void countTimerUpdated(String timerType, int counter) {
        clientController.sendTCP("duel:" + timerType + "," + counter);
    }

    @Override
    public void userLeft(IUser user) {
        clientController.sendTCP("duel:left," + user.getUsername());
    }

    @Override
    public void userReadyUp(IUser user) {
        clientController.sendTCP("duel:ready," + user.getUsername());
    }

    @Override
    public void userCount(IUser user, int count) {
        clientController.sendTCP("duel:count," + user.getUsername() + "," + count);
    }

    @Override
    public IUser getUser() {
        return user;
    }

    public boolean hasActiveDuel() {
        return duel != null;
    }

}
