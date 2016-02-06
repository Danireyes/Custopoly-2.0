package com.iplusplus.custopoly.model.gamemodel.command;

import com.iplusplus.custopoly.controller.observer.Controller;
import com.iplusplus.custopoly.model.gamemodel.element.Game;
import com.iplusplus.custopoly.model.gamemodel.element.Player;
import com.iplusplus.custopoly.model.gamemodel.element.PropertyLand;

/**
 * Created by usuario-pc on 14/12/2015.
 */
public class PlayerLoseCommand implements Command {
    private Player player = null;

    public PlayerLoseCommand(Player player) {
        this.player = player;
    }
    @Override
    public void execute(Controller c) {
        if (player == null)
            this.withoutPlayer(c);
        else
            this.withPlayer(c);
    }


    private void withPlayer(Controller c) {
        for (PropertyLand land : player.getMortgagedProperties()) {
            new MortgageCommand(land).execute(c);
        }
        for(PropertyLand land : player.getProperties()) {
            land.setAssignment(new PurchasableCommand());
        }
        c.getGame().removePlayer(player);
    }

    private void withoutPlayer (Controller c) {
        for (PropertyLand land : c.getGame().getCurrentPlayer().getMortgagedProperties()) {
            new MortgageCommand(land).execute(c);
        }
        for(PropertyLand land : c.getGame().getCurrentPlayer().getProperties()) {
            land.setAssignment(new PurchasableCommand());
        }
        c.getGame().removePlayer(c.getGame().getCurrentPlayer());
    }


}
