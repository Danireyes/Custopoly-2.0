package com.iplusplus.custopoly.model.gamemodel.command;

import android.content.Context;

import com.iplusplus.custopoly.controller.observer.Controller;
import com.iplusplus.custopoly.model.gamemodel.element.Game;

public class EmptyCommand implements Command {

    @Override
    public void execute(Controller c) {
        // Intentionally left blank
    }
}
