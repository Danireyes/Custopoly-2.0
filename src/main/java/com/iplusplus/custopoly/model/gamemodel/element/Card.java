package com.iplusplus.custopoly.model.gamemodel.element;

import com.iplusplus.custopoly.model.gamemodel.command.Command;

import java.io.Serializable;

public class Card implements Serializable {
	
	public String text;
	public Command command;
    public String type;

    public Card(String text, Command command, String type) {
        this.text = text;
        this.command = command;
        this.type = type;
    }

    public String getText() {
        return text;
    }

    public Command getCommand() {
        return command;
    }

    public String getType() {
        return type;
    }
}
