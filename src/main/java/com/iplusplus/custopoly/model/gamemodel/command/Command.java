package com.iplusplus.custopoly.model.gamemodel.command;

import com.iplusplus.custopoly.controller.observer.Controller;

import java.io.Serializable;

public interface Command extends Serializable {

    void execute(Controller controller);

}
