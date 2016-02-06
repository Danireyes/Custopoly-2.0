package com.iplusplus.custopoly.model.gamemodel.element;

import com.iplusplus.custopoly.model.PlayerSkin;
import com.iplusplus.custopoly.model.gamemodel.behaviour.ConstructionAllowance;
import com.iplusplus.custopoly.model.gamemodel.util.Color;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class Player implements Serializable {
    //Functionality fields
    private ArrayList<Card> cardsOwned;
    private ArrayList<PropertyLand> propertiesOwned;
    private ArrayList<PropertyLand> propertiesMortgaged;
    private int playerID, playerBalance, turnsInJail;
    private int landIndex; //Position in the internal representation of the board

    //Appearance-realated fields
    private String playerName;
    private PlayerSkin skin;
    private final int INITIAL_PAYMENT = 50000;

    //ids of properties
    private ArrayList<Integer> idsOfUnmortgaged;
    private ArrayList<Integer> idsOfMortgaged;

    public Player(int playerID, String playerName, int landIndex, PlayerSkin skin) {
        this.playerID = playerID;
        this.playerName = playerName;
        this.landIndex = landIndex;
        this.skin = skin;
        this.playerBalance = INITIAL_PAYMENT;
        this.propertiesOwned = new ArrayList<PropertyLand>();
        this.propertiesMortgaged = new ArrayList<PropertyLand>();
        this.turnsInJail = 0;
    }

    public boolean isGameOver() {
        return (this.getPriceOfMortgageOfPropertiesOwned() + this.getBalance()) <= 0;
    }

    public int getTurnsInJail() {
        return this.turnsInJail;
    }

    public void enterInJail() {
        this.turnsInJail += 3;
    }

    public void decreaseTurnInJail() {
        this.turnsInJail -= 1;
    }

    public void getOutOfJail() {
        this.turnsInJail = 0;
    }

    public String getName() {
        return this.playerName;
    }

    public int getPlayerID() {
        return this.playerID;
    }

    public void increaseBalance(int amount) {
        this.playerBalance += amount;
    }

    public void decreaseBalance(int amount) {
        this.playerBalance -= amount;
    }

    public int getBalance() {
        return this.playerBalance;
    }

    public ArrayList<PropertyLand> getProperties() {
        return this.propertiesOwned;
    }

    public int getPriceOfMortgageOfPropertiesOwned() {
        int price = 0;
        for (PropertyLand land : this.propertiesOwned) {
            price += land.getMortgage();
        }
        return price;
    }

    public ArrayList<Card> getCardsOwned() {
        return this.cardsOwned;
    }

    public void addProperty(PropertyLand land) {
        int i = 0;
        if (this.propertiesOwned.size() == 0) {
            this.propertiesOwned.add(land);
        }
        else {
            if(land instanceof ColoredLand) {
                updateConstructionAllowanceinBuy((ColoredLand) land);
            }
            while (this.propertiesOwned.get(i).getLandIndex() < land.getLandIndex()) {
                i++;
                if(i == this.propertiesOwned.size()) {
                    break;
                }
            }
            this.propertiesOwned.add(i,land);
        }
    }

    public void discardProperty(Land land) {
        if(land instanceof ColoredLand) {
            updateConstructionAllowanceinSell((ColoredLand) land);
        }
        this.propertiesOwned.remove(land);
    }

    public ArrayList<PropertyLand> getMortgagedProperties() {
        return propertiesMortgaged;
    }

    public void addCard(Card card) {
        this.cardsOwned.add(card);
    }

    public void useCard(Card card) {// TODO implement
        this.cardsOwned.remove(card);
    }

    public PlayerSkin getSkin() {
        return this.skin;
    }

    public void setSkin(PlayerSkin skin) {
        this.skin = skin;
    }

    public int getLandIndex() {
        return landIndex;
    }

    public void setLandIndex(int landIndex) {
        this.landIndex = landIndex;
    }

    private void updateConstructionAllowanceinBuy(ColoredLand land) {
        ColoredLand l2 = null;
        if (land.getColor() == -2872491 || land.getColor() == -15750672) { // If it is green or orange
            for (PropertyLand l : this.propertiesOwned) {
                if (l instanceof ColoredLand) {
                    l2 = (ColoredLand) l;
                    if (l2.getColor() == land.getColor()) {
                        l2.setConstructionBehavior(ConstructionAllowance.CONSTRUCTION_ALLOWED);
                        land.setConstructionBehavior(ConstructionAllowance.CONSTRUCTION_ALLOWED);
                    }
                }
            }
        } else {
            boolean first = false;
            for (PropertyLand l : this.propertiesOwned) {
                ColoredLand l3;
                if (l instanceof ColoredLand) {
                    if (!first) {
                        l2 = (ColoredLand) l;
                        if (l2.getColor() == land.getColor()) {
                            first = true;
                        }
                    } else {
                        l3 = (ColoredLand) l;
                        if (l3.getColor() == land.getColor()) {
                            l2.setConstructionBehavior(ConstructionAllowance.CONSTRUCTION_ALLOWED);
                            l3.setConstructionBehavior(ConstructionAllowance.CONSTRUCTION_ALLOWED);
                            land.setConstructionBehavior(ConstructionAllowance.CONSTRUCTION_ALLOWED);
                        }
                    }
                }
            }
        }
    }

    private void updateConstructionAllowanceinSell(ColoredLand land) {
        ColoredLand l2;
        for (PropertyLand l : this.propertiesOwned) {
            if (l instanceof ColoredLand) {
                l2 = (ColoredLand) l;
                if (l2.getColor() == land.getColor()) {
                    l2.setConstructionBehavior(ConstructionAllowance.CONSTRUCTION_DENIED);
                }
            }
        }
    }


    private void changeConstructionBehaviour(Land land) {
        if (land.getConstructionBehavior() == ConstructionAllowance.CONSTRUCTION_ALLOWED) {
            land.setConstructionBehavior(ConstructionAllowance.CONSTRUCTION_DENIED);
        } else {
            land.setConstructionBehavior(ConstructionAllowance.CONSTRUCTION_ALLOWED);
        }
    }

    private boolean lookFor(int index) {
        boolean found = false;
        int i = 0;
        while (!found && i < this.propertiesOwned.size()) {
            if (this.propertiesOwned.get(i).getLandIndex() == index) {
                found = true;
            }
            i++;
        }
        return found;
    }

    public PropertyLand getPropertyByIndex(int index) {
        boolean found = false;
        int i = 0;
        while (!found && i < this.propertiesOwned.size()) {
            if (this.propertiesOwned.get(i).getLandIndex() == index) {
                return this.propertiesOwned.get(i);
            }
            i++;
        }
        return null;
    }
}


