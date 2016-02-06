package com.iplusplus.custopoly.model.gamemodel.command;

import com.iplusplus.custopoly.controller.observer.Controller;
import com.iplusplus.custopoly.model.gamemodel.element.ColoredLand;
import com.iplusplus.custopoly.model.gamemodel.element.Game;

/**
 * Created by usuario-pc on 19/12/2015.
 */
public class SellHouseCommand implements Command {
    @Override
    public void execute(Controller c) {
        Game game = c.getGame();
        ColoredLand land = (ColoredLand) game.getCurrentPlayer().getPropertyByIndex(game.getCurrentPlayer().getLandIndex());
        land.getBuildingHolder().removeHouse();
        if (land.getBuildingHolder().getHouseAmount() == 4) {
            game.getCurrentPlayer().increaseBalance(land.getHotelPrice() / 2);
        }
        else {
            game.getCurrentPlayer().increaseBalance(land.getHousePrice() / 2);
        }
    }
}
