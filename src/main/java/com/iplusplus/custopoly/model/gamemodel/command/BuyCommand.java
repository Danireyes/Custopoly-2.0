package com.iplusplus.custopoly.model.gamemodel.command;

import com.iplusplus.custopoly.controller.observer.Controller;
import com.iplusplus.custopoly.model.gamemodel.element.Game;
import com.iplusplus.custopoly.model.gamemodel.element.Player;
import com.iplusplus.custopoly.model.gamemodel.element.PropertyLand;

/**
 * Created by fran on 02/05/2015.
 */
public class BuyCommand implements Command {


    @Override
    public void execute(Controller c) {
        Game game = c.getGame();
        PropertyLand land = (PropertyLand) game.getBoard().getLands().get(game.getCurrentPlayer().getLandIndex());
        Player player = game.getCurrentPlayer();
        if (player.getBalance() > land.getPrice()) {
            player.decreaseBalance(land.getPrice());
            player.addProperty(land);
            land.setAssignment(new PayRentCommand());
            c.onBoughtCard(game.getCurrentPlayer(), land);
        }
        else {
            c.onBuyError(game.getCurrentPlayer(), land);
        }

    }

}
