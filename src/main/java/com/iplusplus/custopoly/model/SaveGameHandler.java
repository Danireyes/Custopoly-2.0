package com.iplusplus.custopoly.model;

import java.io.IOException;
import android.content.Context;

import com.iplusplus.custopoly.Custopoly;
import com.iplusplus.custopoly.model.gamemodel.element.Bank;
import com.iplusplus.custopoly.model.gamemodel.element.Board;
import com.iplusplus.custopoly.model.gamemodel.element.Game;
import com.iplusplus.custopoly.model.gamemodel.element.Player;
import com.iplusplus.custopoly.view.R;

import java.io.*;
import java.util.ArrayList;

/**
 * Singleton class in charge of saving and loading Custopoly games from memory
 *
 * @author Fran Lozano
 *
 */

public class SaveGameHandler {

    private String GENERIC_GAME_NAME = Custopoly.getAppContext().getString(R.string.generic_game_name);

    private static SaveGameHandler INSTANCE;

    /**
     * Constructor of the class. Private as it's a Singleton class
     */
    private SaveGameHandler() {
    }

    /**
     * Factory Method for Singleton object.
     *
     * @return Returns the single instance of SaveGameHandler that there should be.
     * It initializes INSTANCE if it's not initialized, and then return it.
     */
    public static SaveGameHandler getInstance() {
        if (INSTANCE == null) {
            // Create the instance
            INSTANCE = new SaveGameHandler();
        }
        return INSTANCE;
    }

    /**
     * Save a Custopoly game to a file.
     *
     * @param game The {@link Game} to save
     * @param name The file name to save this game under
     * @throws IOException When there is an error saving the game
     */
    public void saveGame(Game game, String name) throws IOException {
        Context context = Custopoly.getAppContext();
        FileOutputStream fos = context.openFileOutput(name,
                Context.MODE_PRIVATE);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.close();
        fos.close();
    }

    /**
     * Save a Custopoly game to a file. NO NAME
     *
     * @param game The {@link com.iplusplus.custopoly.model.gamemodel.element.Game} to save.
     * @throws IOException When there is an error saving the game
     */
    public void saveGame(Game game) throws IOException {
        Context context = Custopoly.getAppContext();
        FileOutputStream fos = context.openFileOutput(this.GENERIC_GAME_NAME,
                Context.MODE_PRIVATE);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(game.getBank());
        oos.writeObject(game.getBoard());
        oos.writeObject(game.getPlayers());
        int index = (game.getPlayers().indexOf(game.getCurrentPlayer()) + 1) % game.getPlayers().size();
        Player currentPlayer = game.getPlayers().get(index);
        oos.writeObject(currentPlayer);
        oos.writeObject(game.getCurrentTheme());
        oos.close();
        fos.close();
    }

    /**
     * Load a game from a file
     *
     * @param name The file name from which the game should be loaded
     * @return A {@link Game}, representing the loaded game
     * @throws IOException When there is an error while loading the game
     * @throws ClassNotFoundException When there is an error while loading the game
     */
    public Game loadGame(String name)
            throws IOException, ClassNotFoundException {
        Context context = Custopoly.getAppContext();
        File file = new File(context.getFilesDir(), name);
        if (file.exists()) {
            FileInputStream fis;
            fis = context.openFileInput(name);
            ObjectInputStream ois = new ObjectInputStream(fis);
            ois.readFields();
            Game game = (Game) ois.readObject();
            ois.close();
            fis.close();
            return game;
        }
        return null;
    }
    /**
     * Load a game from a file, the game saved under the generic_game_name resource name.
     *
     * @return A {@link Game}, representing the loaded game
     * @throws IOException            When there is an error while loading the game
     * @throws ClassNotFoundException When there is an error while loading the game
     */
    public Game loadGame()
            throws IOException, ClassNotFoundException {
        Context context = Custopoly.getAppContext();
        File file = new File(context.getFilesDir(), this.GENERIC_GAME_NAME);
        if (file.exists()) {
            FileInputStream fis;
            fis = context.openFileInput(this.GENERIC_GAME_NAME);
            ObjectInputStream ois = new ObjectInputStream(fis);
            Bank bank = (Bank) ois.readObject();
            Board board = (Board) ois.readObject();
            ArrayList<Player> players = (ArrayList<Player>) ois.readObject();
            Player currentPlayer = (Player) ois.readObject();
            GameTheme theme = (GameTheme) ois.readObject();
            Game game = new Game(players, board, theme);
            game.setCurrentPlayer(currentPlayer);
            game.setBank(bank);
            ois.close();
            fis.close();
            return game;
        }
        else
            return null;
    }

}
