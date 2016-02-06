package com.iplusplus.custopoly.model.gamemodel.command;


import com.iplusplus.custopoly.controller.GameController;
import com.iplusplus.custopoly.controller.observer.Controller;
import com.iplusplus.custopoly.model.gamemodel.behaviour.ConstructionAllowance;
import com.iplusplus.custopoly.model.gamemodel.element.ColoredLand;
import com.iplusplus.custopoly.model.gamemodel.element.Game;
import com.iplusplus.custopoly.model.gamemodel.element.Land;
import com.iplusplus.custopoly.model.gamemodel.element.Player;
import com.iplusplus.custopoly.model.gamemodel.element.PropertyLand;

public abstract class MoveTokenCommand implements Command {

    @Override
    public void execute(Controller c) {
        Game game = c.getGame();
        int landIndex = getLandIndex(game);
        Land land = game.getBoard().getLands().get(landIndex);

        Player player = game.getCurrentPlayer();
        checkPassedStart(player.getLandIndex(), landIndex, c);
        player.setLandIndex(landIndex);

        Command command = land.getAssignment();
        command.execute(c);

        if(possibleToBuyHouse(land, game.getCurrentPlayer()))
            c.onPurchasableHouse(game.getCurrentPlayer().getName(), (ColoredLand) land);
    }

    private void checkPassedStart(int oldIndex, int newIndex, Controller c) {
        if (newIndex < oldIndex && isForward()) {
            GetPaidCommand command = new GetPaidCommand();
            command.execute(c);
        }
    }

    private boolean possibleToBuyHouse(Land land, Player player) {
        if(land.getClass().getName().equals("ColoredLand")) {
            if (land.getConstructionBehavior() == ConstructionAllowance.CONSTRUCTION_ALLOWED) {
                ColoredLand cLand = (ColoredLand) land;
                for (PropertyLand pLand : player.getProperties()) {
                    if (pLand.equals(cLand)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    public abstract boolean isForward();

    public abstract int getLandIndex(Game game);


}
