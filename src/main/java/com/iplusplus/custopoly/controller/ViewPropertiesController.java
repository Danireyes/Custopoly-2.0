package com.iplusplus.custopoly.controller;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.View;
import android.widget.TabHost;

import com.iplusplus.custopoly.controller.observer.Controller;
import com.iplusplus.custopoly.model.gamemodel.command.MortgageCommand;
import com.iplusplus.custopoly.view.PropertiesViewActivity;
import com.iplusplus.custopoly.model.GameTheme;
import com.iplusplus.custopoly.model.gamemodel.command.SellHouseCommand;
import com.iplusplus.custopoly.model.gamemodel.element.Board;
import com.iplusplus.custopoly.model.gamemodel.element.ColoredLand;
import com.iplusplus.custopoly.model.gamemodel.element.Game;
import com.iplusplus.custopoly.model.gamemodel.element.Player;
import com.iplusplus.custopoly.model.gamemodel.element.PropertyLand;
import com.iplusplus.custopoly.view.R;

import java.util.ArrayList;

public class ViewPropertiesController implements Controller {

    PropertiesViewActivity activity;
    Game game;

    public ViewPropertiesController (PropertiesViewActivity activity, Game game) {
        this.activity = activity;
        this.game = game;
        onAttached();
    }
    @Override
    public void onAttached() {
        Game g = (Game) game;
        activity.addMortgageButtonOnClickListener(getMortgageButtonListener());
        activity.addSellHouseButtonOnClickListener(getSellHouseButtonListener());
        activity.addViewsTabsOnTabChangeListener(getViewsTabsListener());
        activity.buildPropertiesViewFromInformation(g.getCurrentPlayer().getProperties(), g.getUnMortgagedPropertiesIds(g.getCurrentPlayer()), "UnMortgaged");
        if (g.getCurrentPlayer().getProperties().size() > 0) activity.setMortgagedButtonEnabled(true);
    }

    @Override
    public void onGameBegin(GameTheme theme, Board board, Player currentPlayer) {

    }

    @Override
    public void onGameEnd(Board board, ArrayList<Player> playersList) {

    }

    @Override
    public void onGameReset(GameTheme theme, Board board, Player currentPlayer) {

    }

    @Override
    public void onTurnBegin(Board board, Player player) {

    }

    @Override
    public void onTurnEnd(Board board, Player player) {

    }

    @Override
    public void onViewProperties() {

    }

    @Override
    public void onNegotiation() {

    }

    @Override
    public void onRollDiceBegin(Board board, Player currentPlayer) {

    }

    @Override
    public void onRollDiceEnd(Board board, Player currentPlayer) {

    }

    @Override
    public void onFinishDiceDialog(int diceResult, boolean equal) {

    }

    @Override
    public void onCard(String text, String title) {

    }

    @Override
    public void onPayFee(String message) {

    }

    @Override
    public void onPurchasableCard(String playerName, PropertyLand land) {

    }

    @Override
    public void onPurchasableHouse(String playerName, ColoredLand land) {

    }

    @Override
    public void onBoughtCard(Player name, PropertyLand land) {

    }

    @Override
    public void onBuyError(Player name, PropertyLand land) {

    }

    @Override
    public void onBuyHouseError(Player currentPlayer, ColoredLand land) {

    }

    @Override
    public void onMortgage(PropertyLand mortagedLand) {
        new MortgageCommand(mortagedLand).execute(this);
    }

    @Override
    public Game getGame() {
        return game;
    }

    public View.OnClickListener getSellHouseButtonListener () {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Game g = (Game) game;
                ColoredLand land = (ColoredLand) g.getCurrentPlayer().getPropertyByIndex(g.getCurrentPlayer().getLandIndex());
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setTitle(g.getCurrentPlayer().getName()).setMessage("Do you want to sell a house of " + land.getName()).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        new SellHouseCommand().execute(ViewPropertiesController.this);
                        dialog.dismiss();
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
                activity.showAlertDialog(builder.create());
            }

        };
    }

    public TabHost.OnTabChangeListener getViewsTabsListener() {
        final Game g = (Game) game;
        return new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                if(tabId.equals("UnMortgaged"))
                {
                    activity.setTextOfMortgagedButton("Mortgage");
                    activity.buildPropertiesViewFromInformation(g.getCurrentPlayer().getProperties(), g.getUnMortgagedPropertiesIds(g.getCurrentPlayer()), "UnMortgaged");
                }
                else
                {
                    activity.setTextOfMortgagedButton("Unmortgage");
                    activity.buildPropertiesViewFromInformation(g.getCurrentPlayer().getMortgagedProperties(), g.getMortgagedPropertiesIds(g.getCurrentPlayer()), "Mortgaged");
                }
            }
        };
    }

    public View.OnClickListener getMortgageButtonListener () {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog alert = new AlertDialog.Builder(activity)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle(activity.getString(R.string.ingame_mortagege_title))
                        .setMessage(String.format(activity.getString(R.string.ingame_mortagege_message), activity.getSelectedProperty().getName()))
                        .setPositiveButton(activity.getString(R.string.ingame_buyyesbutton), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //Create a new intent and return OK from the activity
                                new MortgageCommand(activity.getSelectedProperty()).execute(ViewPropertiesController.this);
                                activity.finish();
                            }

                        })
                        .setNegativeButton(activity.getString(R.string.ingame_buynobutton), null)
                        .create();
                activity.showAlertDialog(alert);
            }
        };
    }
}
