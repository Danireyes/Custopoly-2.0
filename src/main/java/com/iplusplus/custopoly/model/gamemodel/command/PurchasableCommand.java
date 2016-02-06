package com.iplusplus.custopoly.model.gamemodel.command;


import com.iplusplus.custopoly.controller.GameController;
import com.iplusplus.custopoly.controller.observer.Controller;
import com.iplusplus.custopoly.model.gamemodel.element.Game;
import com.iplusplus.custopoly.model.gamemodel.element.PropertyLand;

public class PurchasableCommand implements Command {

    /**
     * Dispatch an event to make a property purchasable
     *
     * @param c
     */
    @Override
    public void execute(Controller c) {
        Game game = c.getGame();
        PropertyLand land = (PropertyLand) game.getBoard().getLands().get(game.getCurrentPlayer().getLandIndex());
        c.onPurchasableCard(game.getCurrentPlayer().getName(), land);
    }
}
