package com.iplusplus.custopoly.model.gamemodel.command;

import com.iplusplus.custopoly.controller.GameController;
import com.iplusplus.custopoly.model.gamemodel.element.Card;
import com.iplusplus.custopoly.model.gamemodel.element.Game;

import java.util.ArrayList;

/**
 * Created by usuario-pc on 12/12/2015.
 */
public class DrawEnvelopeCommand extends DrawCardCommand{

    @Override
    protected ArrayList<Card> getCards(Game game) {
        return game.getBank().getEnvelopeCards();
    }
}
