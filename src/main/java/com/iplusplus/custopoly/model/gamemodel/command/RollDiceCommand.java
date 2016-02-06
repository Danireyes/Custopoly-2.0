package com.iplusplus.custopoly.model.gamemodel.command;

import com.iplusplus.custopoly.controller.observer.Controller;
import com.iplusplus.custopoly.model.gamemodel.element.Game;
import com.iplusplus.custopoly.model.gamemodel.element.Player;

public class RollDiceCommand implements Command {

    private int diceResult;
    private boolean equal;

    public RollDiceCommand(int diceResult, boolean equal) {
        this.diceResult = diceResult;
        this.equal = equal;
    }

    @Override
    public void execute(Controller c) {
        Game game = c.getGame();
        Player player = game.getCurrentPlayer();
        if (this.equal)
            player.getOutOfJail();
        if (player.getTurnsInJail() == 0)
            movePlayer(c);
        else
            player.decreaseTurnInJail();
    }

    private void movePlayer(Controller c) {
        MoveForwardCommand moveCommand = new MoveForwardCommand(diceResult);
        moveCommand.execute(c);
    }


}
