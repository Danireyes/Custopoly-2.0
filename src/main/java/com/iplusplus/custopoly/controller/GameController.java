package com.iplusplus.custopoly.controller;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.view.View;

import com.iplusplus.custopoly.controller.observer.Controller;
import com.iplusplus.custopoly.Custopoly;
import com.iplusplus.custopoly.model.gamemodel.command.BuyCommand;
import com.iplusplus.custopoly.model.gamemodel.command.BuyHouseCommand;
import com.iplusplus.custopoly.model.gamemodel.command.EndTurnCommand;
import com.iplusplus.custopoly.model.gamemodel.command.RollDiceCommand;
import com.iplusplus.custopoly.view.DiceFragment;
import com.iplusplus.custopoly.view.GameActivity;
import com.iplusplus.custopoly.view.MainActivity;
import com.iplusplus.custopoly.view.NegotiationActivity;
import com.iplusplus.custopoly.view.PropertiesViewActivity;
import com.iplusplus.custopoly.model.GameTheme;
import com.iplusplus.custopoly.model.SaveGameHandler;
import com.iplusplus.custopoly.model.gamemodel.behaviour.ConstructionAllowance;
import com.iplusplus.custopoly.model.gamemodel.command.PlayerLoseCommand;
import com.iplusplus.custopoly.model.gamemodel.element.Board;
import com.iplusplus.custopoly.model.gamemodel.element.ColoredLand;
import com.iplusplus.custopoly.model.gamemodel.element.Game;
import com.iplusplus.custopoly.model.gamemodel.element.Player;
import com.iplusplus.custopoly.model.gamemodel.element.PropertyLand;
import com.iplusplus.custopoly.view.R;

import java.io.IOException;
import java.util.ArrayList;

import static java.lang.Thread.sleep;

/**
 * Created by usuario-pc on 29/12/2015.
 */
public class GameController implements Controller, DiceFragment.DiceDialogListener{

    private GameActivity activity;
    private Game game;
    boolean finished = false;

    public GameController(GameActivity activity, Game game) {
        this.activity = activity;
        this.game = game;
        onAttached();
    }


    @Override
    public void onGameBegin(GameTheme theme, Board board, Player currentPlayer) {

        activity.drawBoard(theme);
        //drawPlayers(boardLayout);
        activity.drawResources(currentPlayer);

        GameActivity.SquareCell cell = activity.getCells().get(currentPlayer.getLandIndex());
        Runnable runnable = this.getRunnableFor(cell);
        cell.addRunnableToThread(runnable);
        activity.drawPlayers(board);

        activity.setEnabledButton("endTurn", false);
        activity.setEnabledButton("buyButton", true);

        onTurnBegin(board, currentPlayer);
    }

    @Override
    public void onAttached() {
        Game g = (Game) game;
        activity.initSquares(g.getCurrentTheme(), g.getBoard(), g.getPlayers());
        activity.addEndTurnOnClickListener(getEndTurnButtonOnClickListener());
        activity.addMoreOptionsButtonOnClickListener(getMoreOptionsButtonOnClickListener());
        onGameBegin(g.getCurrentTheme(), g.getBoard(), g.getCurrentPlayer());
    }

    @Override
    public void onGameEnd(Board board, ArrayList<Player> playersList) {
        activity.closeGame();
    }

    @Override
    public void onGameReset(GameTheme theme, Board board, Player currentPlayer) {
        activity.closeGame();
    }

    @Override
    public void onTurnBegin(final Board board, final Player player) {
        if (!finished) {
            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
            String turn = player.getName();
            activity.drawResources(player);
            if (player.getTurnsInJail() == 0) {
                builder.setTitle(turn + " turn")
                        .setCancelable(false)
                        .setPositiveButton("Roll Dice", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                                onRollDiceBegin(board, player);
                            }
                        });
            } else {
                builder.setTitle(turn + " turn")
                        .setCancelable(false)
                        .setPositiveButton("Roll Dice to get out of jail", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                                onRollDiceBegin(board, player);
                            }
                        });
            }
            AlertDialog alert = builder.create();
            activity.showAlertDialog(alert);
            activity.setEnabledButton("buyButton", false);
        }
    }

    @Override
    public void onTurnEnd(Board board, Player player) {
        if(game.getPlayers().size() == 1) {
            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
            builder.setTitle(game.getPlayers().get(0).getName()).setMessage("You win!!!. Game is over!!").setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    Intent finished = new Intent(activity, MainActivity.class);
                    activity.finish();
                    activity.startActivity(finished);
                    dialog.dismiss();
                }
            });
            activity.showAlertDialog(builder.create());
            finished = true;
        }
        activity.setEnabledButton("endTurn", false);
        try {
            SaveGameHandler.getInstance().saveGame(game);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onViewProperties() {
            //Change activity
            Intent propertiesView = new Intent(activity, PropertiesViewActivity.class);
            //Set the arguments to pass to the view
            propertiesView.putExtra("game", game);

            activity.startActivity(propertiesView);
    }

    @Override
    public void onNegotiation() {
        //Change activity
        Intent negotiation = new Intent(activity, NegotiationActivity.class);

        //Set the arguments to pass to the view
        Game g = (Game) game;
        negotiation.putExtra("game",  g);
        ArrayList<Player> players = g.getNotCurrentPlayer();

        activity.startActivity(negotiation);
    }

    @Override
    public void onRollDiceBegin(Board board, Player currentPlayer) {
        // DialogFragment.show() will take care of adding the fragment
        // in a transaction.  We also want to remove any currently showing
        // dialog, so make our own transaction and take care of that here.
        FragmentTransaction ft = activity.getFragmentManager().beginTransaction();
        Fragment prev = activity.getFragmentManager().findFragmentByTag("dialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);


        // Create and show the dialog.
        DialogFragment newFragment = DiceFragment.newInstance("dialog", this);
        activity.showFragment(ft, "dialog", newFragment);
    }

    @Override
    public void onRollDiceEnd(Board board, final Player currentPlayer, GameActivity.SquareCell lastCellCurrentPlayer) {
        activity.changePlayerOfCell(currentPlayer);
        Runnable runnable = this.getRunnableFor(lastCellCurrentPlayer);
        lastCellCurrentPlayer.addRunnableToThread(runnable);
        GameActivity.SquareCell cell = activity.getCells().get(currentPlayer.getLandIndex());
        Runnable runnable2 = this.getRunnableFor(cell);
        cell.addRunnableToThread(runnable2);
        activity.drawPlayers(board);
        activity.drawResources(currentPlayer);
        activity.setEnabledButton("endTurn", true);
        if (currentPlayer.getPropertyByIndex(currentPlayer.getLandIndex()) != null) {
            if (currentPlayer.getPropertyByIndex(currentPlayer.getLandIndex()).getConstructionBehavior() == ConstructionAllowance.CONSTRUCTION_ALLOWED) {
                activity.setEnabledButton("buyButton", true);
                activity.setOnClickListenerButton("buyButton", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final Context context = Custopoly.getAppContext();
                        final ColoredLand land = (ColoredLand) currentPlayer.getPropertyByIndex(currentPlayer.getLandIndex());
                        String message = context.getText(R.string.ingame_askWantToBuy).toString();
                        String formatMessage = String.format(message, " a house", land.getHousePrice());
                        String title = currentPlayer.getName();

                        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                        builder.setTitle(title).setMessage(formatMessage).setPositiveButton(context.getString(R.string.ingame_buyyesbutton), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                new BuyHouseCommand().execute(GameController.this);
                                dialog.dismiss();
                            }
                        });
                        builder.setNegativeButton(context.getString(R.string.ingame_buynobutton), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                String s = context.getResources().getText(R.string.ingame_notWantToBuy).toString();
                                String fs = String.format(s, currentPlayer.getName(), land.getName());
                                AlertDialog.Builder builder2 = new AlertDialog.Builder(activity);
                                builder2.setTitle(fs);
                                builder2.setMessage(fs);
                                dialog.dismiss();
                            }
                        });
                        activity.showAlertDialog(builder.create());
                    }

                });
            }

        }
    }

    @Override
    public void onFinishDiceDialog(int diceResult, boolean equal) {
        Game g = (Game) this.game;
        GameActivity.SquareCell lastCellCurrentPlayer = activity.getCells().get(g.getCurrentPlayer().getLandIndex());
        new RollDiceCommand(diceResult, equal).execute(this);
        onRollDiceEnd(g.getBoard(), g.getCurrentPlayer(), lastCellCurrentPlayer);
    }

    @Override
    public void onCard(String text, String title) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage(text)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
        AlertDialog alert = builder.create();
        alert.setTitle(title);
        alert.setCanceledOnTouchOutside(false);
        activity.showAlertDialog(alert);
    }

    @Override
    public void onPayFee(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage(message)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
        AlertDialog alert = builder.create();
        alert.setCanceledOnTouchOutside(false);
        activity.showAlertDialog(alert);
    }

    @Override
    public void onPurchasableCard(final String playerName, final PropertyLand land) {
        activity.setEnabledButton("buyButton", true);
        activity.setOnClickListenerButton("buyButton", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Context context = Custopoly.getAppContext();

                String message = context.getText(R.string.ingame_askWantToBuy).toString();
                String formatMessage = String.format(message, land.getName(), land.getPrice());
                String title = playerName;

                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setTitle(title).setMessage(formatMessage).setPositiveButton(context.getString(R.string.ingame_buyyesbutton), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        new BuyCommand().execute(GameController.this);
                        dialog.dismiss();
                    }
                });
                builder.setNegativeButton(context.getString(R.string.ingame_buynobutton), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        String s = context.getResources().getText(R.string.ingame_notWantToBuy).toString();
                        String fs = String.format(s, playerName, land.getName());
                        AlertDialog.Builder builder2 = new AlertDialog.Builder(activity);
                        builder2.setTitle(fs);
                        builder2.setMessage(fs);
                        dialog.dismiss();
                    }
                });
                activity.showAlertDialog(builder.create());
            }

        });
    }

    @Override
    public void onPurchasableHouse(final String playerName, final ColoredLand land) {
        activity.setEnabledButton("buyButton", true);
        activity.setOnClickListenerButton("buyButton", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Context context = Custopoly.getAppContext();

                String message = context.getText(R.string.ingame_askWantToBuy).toString();
                String formatMessage = String.format(message, "building", land.getHousePrice());
                String title = playerName;

                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setTitle(title).setMessage(formatMessage).setPositiveButton(context.getString(R.string.ingame_buyyesbutton), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        land.getBuildingHolder().addBuilding();
                        dialog.dismiss();
                    }
                });
                builder.setNegativeButton(context.getString(R.string.ingame_buynobutton), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        String s = context.getResources().getText(R.string.ingame_notWantToBuy).toString();
                        String fs = String.format(s, playerName, "the building");
                        AlertDialog.Builder builder2 = new AlertDialog.Builder(activity);
                        builder2.setTitle(fs);
                        builder2.setMessage(fs);
                        dialog.dismiss();
                    }
                });
                activity.showAlertDialog(builder.create());
            }

        });
    }

    @Override
    public void onBoughtCard(Player currentPlayer, PropertyLand land) {
        activity.setEnabledButton("buyButton", false);
    }

    @Override
    public void onBuyError(Player currentPlayer, PropertyLand land) {
        Context context = Custopoly.getAppContext();
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        String s = context.getResources().getText(R.string.ingame_buyFailure).toString();
        String message = String.format(s, currentPlayer.getName());
        builder.setTitle("Not possible to buy").setMessage(message).setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        activity.showAlertDialog(builder.create());
    }

    @Override
    public void onBuyHouseError(Player currentPlayer, ColoredLand land) {
        Context context = Custopoly.getAppContext();
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        String s = context.getResources().getText(R.string.ingame_buyHouseFailure).toString();
        String message = String.format(s, currentPlayer.getName());
        builder.setTitle("Not possible to buy").setMessage(message).setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    @Override
    public void onMortgage(PropertyLand mortagedLand){}

    @Override
    public Game getGame() {
        return game;
    }

    private Handler getHandlerFor(final GameActivity.SquareCell cell) {
        return new Handler() {
            int displayed = 0;
            @Override
            public void handleMessage(Message msg) {

                int index = cell.getIndex();

                if (displayed == 0) {
                    activity.getSkinsViews().get(index).removeAllViews();
                    activity.addViewToCell(cell.getPlayersViews().get(0), index);
                    displayed = 1;
                } else if (displayed == 1 && cell.getPlayerSkins().size() > 1) {
                    activity.getSkinsViews().get(index).removeAllViews();
                    activity.addViewToCell(cell.getPlayersViews().get(1), index);
                    switch (cell.getPlayerSkins().size()) {
                        case 2:
                            displayed = 0;
                            break;
                        default:
                            displayed = 2;
                    }
                } else if (displayed == 2 && cell.getPlayerSkins().size() > 2) {
                    activity.getSkinsViews().get(index).removeAllViews();
                    activity.addViewToCell(cell.getPlayersViews().get(2), index);
                    switch (cell.getPlayerSkins().size()) {
                        case 3:
                            displayed = 0;
                            break;
                        default:
                            displayed = 3;
                    }
                } else if (cell.getPlayerSkins().size() > 3){
                    activity.getSkinsViews().get(index).removeAllViews();
                    activity.addViewToCell(cell.getPlayersViews().get(3), index);
                    displayed = 0;
                }
            }
        };
    }

    public Runnable getRunnableFor(final GameActivity.SquareCell cell) {
        final int numOfPlayers = cell.getPlayerSkins().size();
        if(numOfPlayers > 1) {
            final Handler h = getHandlerFor(cell);
            return new Runnable() {

                @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1)
                @Override
                public void run() {
                    boolean finish = false;

                    try {
                        synchronized (this) {
                            sleep(1000);
                        }
                    } catch (InterruptedException e) {

                    }
                    while (!finish) {
                        if (cell.getPlayerSkins().size() != numOfPlayers) {
                            finish = true;
                        }
                        if (!finish) {
                            synchronized (this) {
                                h.sendEmptyMessage(0);
                            }
                            try {
                                synchronized (this) {
                                    sleep(1000);
                                }
                            } catch (InterruptedException e) {
                            }
                        }
                    }
                }

            };
        }
        else {return null;}
    }

    public View.OnClickListener getMoreOptionsButtonOnClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setTitle(R.string.moreOptions_title)
                        .setItems(R.array.options, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case 0:
                                        this.onViewPropertiesPressed();
                                        break;
                                    case 1:
                                        this.onBuyToOtherPlayerPressed();
                                        break;
                                    case 2:
                                        this.onLeaveGamePressed();
                                        break;
                                }
                                dialog.dismiss();
                            }
                            private void onViewPropertiesPressed() {
                                onViewProperties();
                            }

                            private void onBuyToOtherPlayerPressed() {
                                onNegotiation();
                            }

                            private void onLeaveGamePressed() {
                                Game g = (Game) game;
                                new PlayerLoseCommand(null).execute(GameController.this);
                                activity.getCells().get(g.getCurrentPlayer().getLandIndex()).removePlayerSkin(g.getCurrentPlayer().getSkin().getImageResourceName());
                                new EndTurnCommand().execute(GameController.this);
                            }

                        });
                AlertDialog dialog = builder.create();
                dialog.setCanceledOnTouchOutside(false);
                activity.showAlertDialog(dialog);
            }
        };
    }

    public View.OnClickListener getEndTurnButtonOnClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new EndTurnCommand().execute(GameController.this);
            }
        };
    }

}
