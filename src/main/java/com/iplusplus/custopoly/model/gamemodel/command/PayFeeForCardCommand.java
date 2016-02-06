package com.iplusplus.custopoly.model.gamemodel.command;

import com.iplusplus.custopoly.controller.observer.Controller;
import com.iplusplus.custopoly.model.gamemodel.element.Game;
import com.iplusplus.custopoly.model.gamemodel.element.Player;

public class PayFeeForCardCommand implements Command {

    private int fee;

    public PayFeeForCardCommand(int fee) {
        this.fee = fee;
    }

    @Override
    public void execute(Controller c) {
        Player player = c.getGame().getCurrentPlayer();
        player.decreaseBalance(fee);
    }
}
