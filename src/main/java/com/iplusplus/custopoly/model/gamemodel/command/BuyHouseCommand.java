package com.iplusplus.custopoly.model.gamemodel.command;

import com.iplusplus.custopoly.controller.GameController;
import com.iplusplus.custopoly.controller.observer.Controller;
import com.iplusplus.custopoly.model.gamemodel.element.ColoredLand;
import com.iplusplus.custopoly.model.gamemodel.element.Game;
import com.iplusplus.custopoly.model.gamemodel.element.Player;

/**
 * Created by usuario-pc on 19/12/2015.
 */
public class BuyHouseCommand implements Command {

    @Override
    public void execute(Controller c) {
        Game game = c.getGame();
       ColoredLand land = (ColoredLand) game.getCurrentPlayer().getPropertyByIndex(game.getCurrentPlayer().getLandIndex());
       Player player = game.getCurrentPlayer();
        if (player.getBalance() > land.getHousePrice()) {
            land.getBuildingHolder().addBuilding();
            player.decreaseBalance(land.getHousePrice());
        }
        else {
            c.onBuyHouseError(game.getCurrentPlayer(), land);
        }
    }
}
