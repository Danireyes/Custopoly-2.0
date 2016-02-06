package com.iplusplus.custopoly.model.gamemodel.command;


import android.content.Context;

import com.iplusplus.custopoly.controller.observer.Controller;
import com.iplusplus.custopoly.model.gamemodel.element.Game;

public class GoJailCommand implements Command {

    @Override
    public void execute(Controller c) {
        Game game = c.getGame();
        (new MoveBackwardCommand(20)).execute(c);
        game.getCurrentPlayer().enterInJail();
    }
}
