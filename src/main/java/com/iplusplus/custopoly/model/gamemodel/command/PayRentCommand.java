package com.iplusplus.custopoly.model.gamemodel.command;

import android.content.Context;

import com.iplusplus.custopoly.controller.GameController;
import com.iplusplus.custopoly.Custopoly;
import com.iplusplus.custopoly.controller.observer.Controller;
import com.iplusplus.custopoly.model.gamemodel.element.Game;
import com.iplusplus.custopoly.model.gamemodel.element.Land;
import com.iplusplus.custopoly.model.gamemodel.element.Player;
import com.iplusplus.custopoly.model.gamemodel.element.PropertyLand;
import com.iplusplus.custopoly.model.gamemodel.util.RentCalculator;
import com.iplusplus.custopoly.view.R;

public class PayRentCommand implements Command {

    @Override
    public void execute(Controller c) {
        Game game = c.getGame();
        PropertyLand property = (PropertyLand) getCurrentLand(game);
        int payment = property.acceptCalculator(new RentCalculator(game));
        makeTransaction(c, payment);
    }

    protected Land getCurrentLand(Game game) {
        return game.getBoard().getLands().get(game.getCurrentPlayer().getLandIndex());
    }

    private void makeTransaction(Controller c, int payment) {
        Game game = c.getGame();
        Context context = Custopoly.getAppContext();
        Player source = game.getCurrentPlayer();
        Player target = game.getOwner((PropertyLand) getCurrentLand(game));
        if (!source.equals(target) && source != null && target != null) {
            source.decreaseBalance(payment);
            target.increaseBalance(payment);
            String message = String.format(context.getText(R.string.ingame_rentpaidmsg).toString(),
                                            source.getName(), payment, target.getName());
            c.onPayFee(message);
        }
    }
}
