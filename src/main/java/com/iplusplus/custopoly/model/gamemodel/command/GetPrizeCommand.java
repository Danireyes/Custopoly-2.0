package com.iplusplus.custopoly.model.gamemodel.command;

import android.content.Context;

import com.iplusplus.custopoly.controller.observer.Controller;
import com.iplusplus.custopoly.model.gamemodel.element.Game;
import com.iplusplus.custopoly.model.gamemodel.element.Player;

public class GetPrizeCommand implements Command {

    private int prize;

    public GetPrizeCommand(int prize) {
        this.prize = prize;
    }

    @Override
    public void execute(Controller c) {
        Game game = c.getGame();
        Player player = game.getCurrentPlayer();
        player.increaseBalance(prize);
    }
}
