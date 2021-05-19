package me.gustavwww.model.duel;

import me.gustavwww.controller.ClientController;
import me.gustavwww.model.IUser;
import me.gustavwww.services.protocol.IServerProtocol;
import me.gustavwww.services.protocol.ServerProtocolFactory;

public class ClientDuelHandler implements IMatchListener {

    private final ClientController clientController;
    private final IUser user;
    private final MatchManager matchManager;
    private final IServerProtocol serverProtocol;
    private final DuelController duelController;

    private DuelRequest request = null;

    public ClientDuelHandler(ClientController clientController, IUser user) {
        this.clientController = clientController;
        this.user = user;
        matchManager = MatchManager.getInstance();
        serverProtocol = ServerProtocolFactory.getServerProtocol();
        duelController = new DuelController(user, clientController);
    }

    public void allowRequests() {
        matchManager.addListener(this);
    }

    public void denyRequests() {
        matchManager.removeListener(this);
    }

    public void sendRequest(String nickname) {
        if (duelController.getDuel() != null || request != null) {
            clientController.sendTCP(serverProtocol.writeError("Already in a duel or has a pending request."));
            return;
        }
        matchManager.dualRequest(this, nickname);
    }

    public void acceptRequest() {
        if (request != null) {
            request.acceptRequest(this);
        }
    }

    public void declineRequest() {
        if (request != null) {
            request.declineRequest(this);
        }
    }

    @Override
    public void gotRequest(DuelRequest request) {
        this.request = request;
        if (!request.getSender().equals(this)) {
            clientController.sendTCP("duel:request," + request.getSender().getUser().getNickname());
        }
    }

    @Override
    public void requestNotFound(String nickname) {
        clientController.sendTCP("duel:response,notfound");
    }

    @Override
    public void requestCancelled() {
        request = null;
        clientController.sendTCP("duel:response,cancelled");
    }

    @Override
    public void requestFinished(Duel duel) {
        request = null;
        duelController.joinDuel(duel);
        clientController.sendTCP("duel:response,finished");
    }

    @Override
    public IUser getUser() {
        return user;
    }

    public DuelController getDuelController() {
        return duelController;
    }

}
