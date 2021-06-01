package me.gustavwww.controller.commandhandlers;

import me.gustavwww.controller.ClientController;
import me.gustavwww.services.protocol.Command;
import me.gustavwww.services.protocol.IServerProtocol;

public class SkinHandler extends AbstractCommandHandler {

    public SkinHandler() {
        super("skin");
    }

    @Override
    protected void concreteHandle(Command cmd, ClientController client, IServerProtocol protocol) {
        String[] args = cmd.getArgs();

        if (args.length < 2) {
            client.sendTCP(protocol.writeError("Invalid arguments."));
            return;
        }

        String skinName = args[1];

        try {

            if (args[0].equalsIgnoreCase("buy")) {

                client.buySkin(skinName);
            } else if (args[0].equalsIgnoreCase("equip")) {

                client.equipSkin(skinName);
            }

        } catch (Exception e) {
            client.sendTCP(protocol.writeError(e.getMessage()));
        }

    }

}
