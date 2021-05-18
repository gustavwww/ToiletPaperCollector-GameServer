package me.gustavwww.controller.commandhandlers;

import me.gustavwww.controller.ClientController;
import me.gustavwww.services.protocol.Command;
import me.gustavwww.services.protocol.IServerProtocol;

public class DuelHandler extends AbstractCommandHandler {

    public DuelHandler() {
        super("duel");
    }

    //TODO: Implement
    @Override
    protected void concreteHandle(Command cmd, ClientController client, IServerProtocol protocol) {
        String[] args = cmd.getArgs();

        if (args.length < 1) {
            client.sendTCP(protocol.writeError("Invalid arguments."));
            return;
        } else if (args.length == 1) {

            if (args[0].equalsIgnoreCase("ready")) {
                client.getDuelController().readyUp();
                return;
            }
            if (args[0].equalsIgnoreCase("count")) {
                client.getDuelController().sendCount();
                return;
            }

        } else {
            if (args[0].equalsIgnoreCase("request")) {
                String nickname = args[1];

                client.sendDuelRequest(nickname);
                return;
            }
            if (args[0].equalsIgnoreCase("response")) {
                switch (args[0]) {
                    case "accept":
                        client.acceptDuelRequest();
                        break;
                    default:
                        client.declineDuelRequest();
                }
                return;
            }

        }

    }

}
