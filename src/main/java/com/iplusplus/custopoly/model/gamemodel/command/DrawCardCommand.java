package com.iplusplus.custopoly.model.gamemodel.command;

import com.iplusplus.custopoly.controller.GameController;
import com.iplusplus.custopoly.controller.observer.Controller;
import com.iplusplus.custopoly.model.gamemodel.element.Card;
import com.iplusplus.custopoly.model.gamemodel.element.Game;

import java.util.ArrayList;
import java.util.Random;

public abstract class DrawCardCommand implements Command {

    protected abstract ArrayList<Card> getCards(Game game);

    @Override
    public void execute(Controller c) {
        Game game = c.getGame();
        ArrayList<Card> cards = getCards(game);
        Random rand = new Random();

        // nextInt is normally exclusive of the top value,
        // so add 1 to make it inclusive
        int randomNum = rand.nextInt(cards.size());
        Card drawnCard = cards.get(randomNum);

        c.onCard(drawnCard.text, drawnCard.type);
        drawnCard.command.execute(c);
    }
}
