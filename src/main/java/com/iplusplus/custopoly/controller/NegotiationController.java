package com.iplusplus.custopoly.controller;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.View;
import android.widget.TabHost;

import com.iplusplus.custopoly.controller.observer.Controller;
import com.iplusplus.custopoly.model.gamemodel.command.MakeDealCommand;
import com.iplusplus.custopoly.view.NegotiationActivity;
import com.iplusplus.custopoly.model.GameTheme;
import com.iplusplus.custopoly.model.gamemodel.element.Board;
import com.iplusplus.custopoly.model.gamemodel.element.ColoredLand;
import com.iplusplus.custopoly.model.gamemodel.element.Game;
import com.iplusplus.custopoly.model.gamemodel.element.Player;
import com.iplusplus.custopoly.model.gamemodel.element.PropertyLand;
import com.iplusplus.custopoly.view.R;

import java.util.ArrayList;


public class NegotiationController implements Controller {
    private Game g;
    private NegotiationActivity activity;

    public NegotiationController(Game g, NegotiationActivity activity) {
        this.g = g;
        this.activity = activity;
        onAttached();
    }

    public TabHost.OnTabChangeListener getViewsTabsListener() {
        return new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                Player player1 = g.getNotCurrentPlayer().get(0);
                Player player2 = null;
                Player player3 = null;
                switch (g.getNotCurrentPlayer().size()) {
                    case 2:
                        player2 = g.getNotCurrentPlayer().get(1);
                        break;
                    case 3:
                        player3 = g.getNotCurrentPlayer().get(2);
                        break;
                }
                if (tabId.equals(player1.getName())) {
                    activity.buildPropertiesViewFromInformation(player1.getProperties(), g.getUnMortgagedPropertiesIds(player1), player1.getName());
                } else if (tabId.equals(player2.getName())) {
                    activity.buildPropertiesViewFromInformation(player2.getProperties(), g.getUnMortgagedPropertiesIds(player2), player2.getName());
                } else if (tabId.equals(player3.getName())) {
                    activity.buildPropertiesViewFromInformation(player3.getProperties(), g.getUnMortgagedPropertiesIds(player3), player3.getName());
                }
            }
        };
    }

    public View.OnClickListener getMakOfferButtonListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final PropertyLand selectedProperty = activity.getSelectedProperty();
                AlertDialog alert = new AlertDialog.Builder(activity)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle(g.getCurrentPlayer().getName())
                        .setMessage(String.format(activity.getString(R.string.ingame_makeOffer_message), selectedProperty.getName()))
                        .setPositiveButton(activity.getString(R.string.ingame_buyyesbutton), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //Create a new intent and return OK from the activity
                                /*Intent returnIntent = new Intent();
                                returnIntent.putExtra("mortgageLand", selectedProperty.getName());
                                setResult(RESULT_OK, returnIntent);
                                finish();
                                */
                                final String title;
                                final Player p;
                                switch (activity.getViewsTabs().getId()) {
                                    case R.id.Player2:
                                        p = g.getNotCurrentPlayer().get(1);
                                        title = p.getName();
                                        break;
                                    case R.id.Player3:
                                        p = g.getNotCurrentPlayer().get(2);
                                        title = p.getName();
                                        break;
                                    default:
                                        p = g.getNotCurrentPlayer().get(0);
                                        title = p.getName();
                                }
                                AlertDialog alert2 = new AlertDialog.Builder(activity)
                                        .setIcon(android.R.drawable.ic_dialog_alert)
                                        .setTitle(title)
                                        .setMessage(String.format(activity.getString(R.string.ingame_acceptOffer_message), selectedProperty.getName(), activity.getOffer().getValue()))
                                        .setPositiveButton(activity.getString(R.string.ingame_buyyesbutton), new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                //Create a new intent and return OK from the activity
                                                new MakeDealCommand(selectedProperty,p , activity.getOffer().getValue()).execute(NegotiationController.this);
                                                activity.finish();
                                            }

                                        })
                                        .setNegativeButton(activity.getString(R.string.ingame_buynobutton), null)
                                        .create();
                                activity.showAlertDialog(alert2);
                            }

                        })
                        .setNegativeButton(activity.getString(R.string.ingame_buynobutton), null)
                        .create();
                activity.showAlertDialog(alert);
            }
        };
    }

    @Override
    public void onAttached() {
        ArrayList<Player> players = g.getNotCurrentPlayer();

        final TabHost.TabSpec player1Tab = activity.getViewsTabs().newTabSpec(players.get(0).getName());
        player1Tab.setContent(R.id.Player1);
        player1Tab.setIndicator(players.get(0).getName());
        activity.getViewsTabs().addTab(player1Tab);

        if (players.size() == 2) {

            TabHost.TabSpec player2Tab = activity.getViewsTabs().newTabSpec(players.get(1).getName());
            player2Tab.setContent(R.id.Player2);
            player2Tab.setIndicator(players.get(1).getName());
            activity.getViewsTabs().addTab(player2Tab);
        }

        if (players.size() == 3) {

            //Create tab2 and add it
            TabHost.TabSpec player2Tab = activity.getViewsTabs().newTabSpec(players.get(1).getName());
            player2Tab.setContent(R.id.Player2);
            player2Tab.setIndicator(players.get(1).getName());
            activity.getViewsTabs().addTab(player2Tab);

            //Create tab3 and add it
            TabHost.TabSpec player3Tab = activity.getViewsTabs().newTabSpec(players.get(2).getName());
            player3Tab.setContent(R.id.Player3);
            player3Tab.setIndicator(players.get(2).getName());
            activity.getViewsTabs().addTab(player3Tab);
        }
        activity.setPlayers(players);
        activity.setViewsTabsOnTabChangeListener(getViewsTabsListener());
        activity.addMakeOfferButtonOnClickListener(getMakOfferButtonListener());
        activity.addOfferMaxValue(g.getCurrentPlayer().getBalance());
        activity.buildPropertiesViewFromInformation(players.get(0).getProperties(), g.getUnMortgagedPropertiesIds(players.get(0)), players.get(0).getName());
    }

    @Override
    public void onGameBegin(GameTheme theme, Board board, Player currentPlayer) {}

    @Override
    public void onGameEnd(Board board, ArrayList<Player> playersList) {}

    @Override
    public void onGameReset(GameTheme theme, Board board, Player currentPlayer) {}

    @Override
    public void onTurnBegin(Board board, Player player) {}

    @Override
    public void onTurnEnd(Board board, Player player) {}

    @Override
    public void onViewProperties() {}

    @Override
    public void onNegotiation() {}

    @Override
    public void onRollDiceBegin(Board board, Player currentPlayer) {}

    @Override
    public void onRollDiceEnd(Board board, Player currentPlayer) {}

    @Override
    public void onFinishDiceDialog(int diceResult, boolean equal) {}

    @Override
    public void onCard(String text, String title) {}

    @Override
    public void onPayFee(String message) {}

    @Override
    public void onPurchasableCard(String playerName, PropertyLand land) {}

    @Override
    public void onPurchasableHouse(String playerName, ColoredLand land) {}

    @Override
    public void onBoughtCard(Player name, PropertyLand land) {}

    @Override
    public void onBuyError(Player name, PropertyLand land) {}

    @Override
    public void onBuyHouseError(Player currentPlayer, ColoredLand land) {}

    @Override
    public void onMortgage(PropertyLand mortagedLand) {}

    @Override
    public Game getGame() {
        return g;
    }


}
