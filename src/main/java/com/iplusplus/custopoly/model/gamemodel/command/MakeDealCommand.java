package com.iplusplus.custopoly.model.gamemodel.command;

import com.iplusplus.custopoly.controller.observer.Controller;
import com.iplusplus.custopoly.model.gamemodel.element.Game;
import com.iplusplus.custopoly.model.gamemodel.element.Player;
import com.iplusplus.custopoly.model.gamemodel.element.PropertyLand;

/**
 * Created by usuario-pc on 19/12/2015.
 */
public class MakeDealCommand implements Command {
    private PropertyLand pl;
    private Player p;
    private int price;

    public MakeDealCommand(PropertyLand pl, Player p, int price) {
        this.pl = pl;
        this.price = price;
        this.p = p;
    }
    @Override
    public void execute(Controller c) {
        Game game = c.getGame();
        p.discardProperty(pl);
        p.increaseBalance(price);
        game.getCurrentPlayer().addProperty(pl);
        game.getCurrentPlayer().decreaseBalance(price);
    }
}
