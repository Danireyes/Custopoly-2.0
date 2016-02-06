package com.iplusplus.custopoly.model.gamemodel.element;

import com.iplusplus.custopoly.model.GameTheme;
import com.iplusplus.custopoly.model.exceptions.PlayerNotFoundException;

import java.io.Serializable;
import java.util.ArrayList;

public class Game implements Serializable {

    //Attributes

    private static Bank bank;
    private static Board board;
    private static ArrayList<Player> playersList;

    private static Player currentPlayer;
    private static GameTheme theme;

    public final int PAYMENT = 500;

    //Constructor
    public Game(ArrayList<Player> playersList, Board board, GameTheme theme) {
        initBank(board.getLands());
        this.board = board;
        this.playersList = playersList;
        this.currentPlayer = playersList.get(0);
        this.theme = theme;
    }

    public void setBank(Bank bank) {
        Game.bank = bank;
    }

    // //
    //GameObservable Methods (From GameFactory)
    // //


    // //
    //Private Methods
    // //

    private void initBank(ArrayList<Land> lands) {
        ArrayList<PropertyLand> propertyLands = new ArrayList<PropertyLand>();
        for (Land land : lands) {
            if (land instanceof PropertyLand) {
                propertyLands.add((PropertyLand) land);
            }
        }
        bank = new Bank(propertyLands);
    }

    //Auxiliary methods for the getters
    private Player findPlayerById(int playerId) {
        Player player = null;
        for (Player p : this.playersList) {
            if (p.getPlayerID() == playerId) {
                player = p;
            }
        }
        if (player == null) throw new PlayerNotFoundException();
        return player;
    }

    private Player findPlayerByName(String playerName) {
        Player player = null;
        for (Player p : this.playersList) {
            if (p.getName().equals(playerName)) {
                player = p;
            }
        }
        if (player == null) throw new PlayerNotFoundException();
        return player;
    }

    public Bank getBank() {
        return this.bank;
    }

    // //
    // Methods implemented from GameFacade
    // //


    public ArrayList<String> getAssetsOwnedByCurrentPlayer() {
        ArrayList<String> names = new ArrayList<String>();
        for (PropertyLand pr : this.currentPlayer.getProperties()) {
            names.add(pr.getName());
        }
        return names;
    }


    public ArrayList<String> getAssetNamesOwnedByPlayer(int playerId) {
        Player player = findPlayerById(playerId);
        ArrayList<String> names = new ArrayList<String>();
        for (PropertyLand pr : player.getProperties()) {
            names.add(pr.getName());
        }
        return names;
    }


    public ArrayList<String> getAssetNamesOwnedByPlayer(String playerName) {
        Player player = findPlayerByName(playerName);
        ArrayList<String> names = new ArrayList<String>();
        for (PropertyLand pr : player.getProperties()) {
            names.add(pr.getName());
        }
        return names;
    }


    public String getOwnerName(String propertyName) {
        String name = null;
        for (Player p : this.playersList) {
            for (PropertyLand pr : p.getProperties()) {
                if (pr.getName().equals(propertyName))
                    name = p.getName();
            }
        }
        if (name == null) throw new PlayerNotFoundException();
        return name;
    }

    public int getOwnerId(String propertyName) {
        int id = -1;
        for (Player p : this.playersList) {
            for (PropertyLand pr : p.getProperties()) {
                if (pr.getName().equals(propertyName))
                    id = p.getPlayerID();
            }
        }
        if (id == -1) throw new PlayerNotFoundException();
        return id;
    }


    public int getBoardSize() {
        return this.board.getSize();
    }


    public ArrayList<String> getPlayerNames() {
        ArrayList<String> names = new ArrayList<String>();
        for (Player p : this.playersList) {
            names.add(p.getName());
        }
        return names;
    }


    public int getPlayerIdByName(String playerName) {
        return findPlayerByName(playerName).getPlayerID();
    }


    public String getPlayerNameById(int playerID) {
        return findPlayerById(playerID).getName();
    }

    public Player anyHasLost() {
        for (Player player : this.playersList) {
            if (player.isGameOver()) {
                return player;
            }
        }
        return null;
    }

    public boolean isEnded() {
        return this.getPlayers().size() == 1;
    }


    public String getWinnerName() {
        return this.getPlayers().get(0).getName();
    }


    public Player getCurrentPlayer() {
        return currentPlayer;
    }


    public void setCurrentPlayer(Player player) {
        this.currentPlayer = player;
    }


    public ArrayList<Player> getPlayers() {
        return playersList;
    }


    public String getCurrentPlayerSkinResPath() {
        return this.currentPlayer.getSkin().getImageResourceName();
    }


    public String getPlayerSkinResPathById(int playerId) {
        return findPlayerById(playerId).getSkin().getImageResourceName();
    }


    public String getPlayerSkinResPathByName(String playerName) {
        return findPlayerByName(playerName).getSkin().getImageResourceName();
    }


    public String getCurrentPlayerName() {
        return this.currentPlayer.getName();
    }


    public int getCurrentPlayerId() {
        return this.currentPlayer.getPlayerID();
    }


    public int getPlayerBalanceById(int playerId) {
        return findPlayerById(playerId).getBalance();
    }


    public int getPlayerBalanceByName(String playerName) {
        return findPlayerByName(playerName).getBalance();
    }


    public int getCurrentPlayerBalance() {
        return this.currentPlayer.getBalance();
    }


    public ArrayList<String> getCurrentPlayerCardList() {

        return null;
    }

    public ArrayList<Player> getNotCurrentPlayer() {
        ArrayList<Player> players = (ArrayList<Player>) playersList.clone();
        for (Player p : players) {
            if (p.getName().equals(this.currentPlayer.getName())) {
                players.remove(p);
                break;
            }
        }
        return players;
    }

    public ArrayList<String> getPlayerCardListById(int playerId) {
        return null;
    }


    public ArrayList<String> getPlayerCardListByName(String playerName) {
        return null;
    }


    public GameTheme getCurrentTheme() {
        return theme;
    }



    public ArrayList<PropertyLand> getAssetsOwnedByPlayer() {
        return currentPlayer.getProperties();
    }

    public Player getOwner(PropertyLand property) {
        for (Player player : playersList) {
            if (player.getProperties().contains(property))
                return player;
        }
        return null;
    }

    public Board getBoard() {
        return board;
    }

    public void removePlayer(Player player) {
        this.playersList.remove(player);
    }

    public ArrayList<Integer> getUnMortgagedPropertiesIds(Player player) {
        ArrayList<Integer> imageIdsUnMortgaged = new ArrayList<>();
        for(PropertyLand prop:player.getProperties())
        {
            imageIdsUnMortgaged.add(theme.getCellResource(prop.getLandIndex()));
        }
        return imageIdsUnMortgaged;
    }

    public ArrayList<Integer> getMortgagedPropertiesIds(Player player) {
        ArrayList<Integer> imageIdsMortgaged = new ArrayList<>();
        for(PropertyLand prop:player.getMortgagedProperties()) {
            imageIdsMortgaged.add(theme.getCellResource(prop.getLandIndex()));
        }
        return imageIdsMortgaged;
    }
   /* //Parcelable methods

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {


        //dest.writeList(observersList);

        dest.writeSerializable(bank);
        dest.writeSerializable(board);
        dest.writeList(playersList);
        dest.writeSerializable(currentPlayer);
        dest.writeSerializable(theme);
    }

    public Game(Parcel source) {
        //source.readArrayList(GameObserver.class.getClassLoader());

        bank = (Bank) source.readSerializable();
        board = (Board) source.readSerializable();
        playersList = new ArrayList<Player>();
        source.readList(playersList, null);
        currentPlayer = (Player) source.readSerializable();
        theme = (GameTheme) source.readSerializable();

    }

    public static final Creator<Game> CREATOR =
            new Creator<Game>() {

                @Override
                public Game createFromParcel(Parcel source) {
                    return new Game(source);
                }

                @Override
                public Game[] newArray(int size) {
                    return new Game[size];
                }
            };*/
}
