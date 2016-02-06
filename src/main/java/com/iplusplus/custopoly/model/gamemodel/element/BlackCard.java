package com.iplusplus.custopoly.model.gamemodel.element;

import com.iplusplus.custopoly.model.gamemodel.behaviour.ConstructionAllowance;
import com.iplusplus.custopoly.model.gamemodel.command.DrawBlackCardCommand;

import java.io.Serializable;


/**
 * Created by usuario-pc on 12/12/2015.
 */
public class BlackCard extends SpecialLand implements Serializable{

    public BlackCard() {
        setConstructionBehavior(ConstructionAllowance.CONSTRUCTION_DENIED);
        setAssignment(new DrawBlackCardCommand());
    }
    @Override
    public String getName() {
        return "BlackCard";
    }
}
