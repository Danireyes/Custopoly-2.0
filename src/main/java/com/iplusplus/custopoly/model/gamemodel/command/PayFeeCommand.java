package com.iplusplus.custopoly.model.gamemodel.command;

import android.content.Context;

import com.iplusplus.custopoly.controller.GameController;
import com.iplusplus.custopoly.Custopoly;
import com.iplusplus.custopoly.controller.observer.Controller;
import com.iplusplus.custopoly.model.gamemodel.element.Game;
import com.iplusplus.custopoly.model.gamemodel.element.Player;
import com.iplusplus.custopoly.view.R;

public class PayFeeCommand implements Command {

    private int fee;

    public PayFeeCommand(int fee) {
        this.fee = fee;
    }

    @Override
    public void execute(Controller c) {
        Game game = c.getGame();
        Context context = Custopoly.getAppContext();
        Player player = game.getCurrentPlayer();
        player.decreaseBalance(fee);
        String message = String.format(context.getText(R.string.ingame_feepaidmsg).toString(), player.getName(), fee);
        c.onPayFee(message);
    }
}
