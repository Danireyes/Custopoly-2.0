package com.iplusplus.custopoly.model.gamemodel.command;

import com.iplusplus.custopoly.controller.GameController;
import com.iplusplus.custopoly.controller.observer.Controller;
import com.iplusplus.custopoly.model.gamemodel.element.Game;

public class EndTurnCommand implements Command {


    /**
     * This method passes the turn to the next player and tells the controller to update itself.
     *
     * @param c
     */
    @Override
    public void execute(Controller c) {
        Game game = c.getGame();
        while (game.anyHasLost() != null) {
            new PlayerLoseCommand(game.anyHasLost()).execute(c);
        }

        int index = (game.getPlayers().indexOf(game.getCurrentPlayer()) + 1) % game.getPlayers().size();

        game.setCurrentPlayer(game.getPlayers().get(index));
        c.onTurnEnd(game.getBoard(), game.getCurrentPlayer());
        c.onTurnBegin(game.getBoard(), game.getCurrentPlayer());
    }
}
