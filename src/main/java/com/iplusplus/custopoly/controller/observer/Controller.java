package com.iplusplus.custopoly.controller.observer;

import android.os.Parcelable;

import com.iplusplus.custopoly.model.GameTheme;
import com.iplusplus.custopoly.model.gamemodel.element.Board;
import com.iplusplus.custopoly.model.gamemodel.element.ColoredLand;
import com.iplusplus.custopoly.model.gamemodel.element.Game;
import com.iplusplus.custopoly.model.gamemodel.element.Player;
import com.iplusplus.custopoly.model.gamemodel.element.PropertyLand;
import com.iplusplus.custopoly.view.GameActivity;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by usuario-pc on 29/12/2015.
 */
public interface Controller {

    public void onAttached();

    //GameObserver Events

    //Game Events
    void onGameBegin(GameTheme theme, Board board, Player currentPlayer);
    void onGameEnd(Board board, ArrayList<Player> playersList);
    void onGameReset(GameTheme theme, Board board, Player currentPlayer);

    //Turn events
    void onTurnBegin(Board board, Player player);
    void onTurnEnd(Board board, Player player);

    //Action events
    void onViewProperties();
    void onNegotiation();

    void onRollDiceBegin(Board board, Player currentPlayer);
    void onRollDiceEnd(Board board, Player currentPlayer, GameActivity.SquareCell lastCellCurrentPlayer);
    void onFinishDiceDialog(int diceResult, boolean equal);

    void onCard(String text, String title);
    void onPayFee(String message);
    void onPurchasableCard(final String playerName, final PropertyLand land);
    void onPurchasableHouse(final String playerName, final ColoredLand land);
    void onBoughtCard(Player name, PropertyLand land);
    void onBuyError(Player name, PropertyLand land);
    void onBuyHouseError(Player currentPlayer, ColoredLand land);
    void onMortgage(PropertyLand mortagedLand);

    Game getGame();
}
