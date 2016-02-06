package com.iplusplus.custopoly.model.gamemodel.command;

import com.iplusplus.custopoly.controller.observer.Controller;
import com.iplusplus.custopoly.model.gamemodel.element.Game;
import com.iplusplus.custopoly.model.gamemodel.element.Player;
import com.iplusplus.custopoly.model.gamemodel.element.PropertyLand;

public class MortgageCommand implements Command {

    private PropertyLand property;

    public MortgageCommand(PropertyLand property) {
        this.property = property;
    }

    @Override
    public void execute(Controller c) {
        Game game = c.getGame();
        Player player = game.getCurrentPlayer();
        if (player.getProperties().contains(property)) {
            doCommand(player, "mortgage");
        } else if (player.getMortgagedProperties().contains(property)) {
            doCommand(player, "unmortgage");
        }
    }

    /**
     * Interpret the result of asking a player about his mortgage.
     *
     * @param player
     * @param command
     */
    private void doCommand(Player player, String command) {
            if (command.equals("mortgage")) {
                player.getProperties().remove(property);
                player.getMortgagedProperties().add(property);
                player.increaseBalance(property.getMortgage());
            } else if (command.equals("unmortgage")) {
                player.getProperties().add(property);
                player.getMortgagedProperties().remove(property);
                player.decreaseBalance(property.getMortgage());
            }
    }
}
