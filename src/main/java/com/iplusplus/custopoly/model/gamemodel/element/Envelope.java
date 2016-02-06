package com.iplusplus.custopoly.model.gamemodel.element;

import com.iplusplus.custopoly.model.gamemodel.behaviour.ConstructionAllowance;
import com.iplusplus.custopoly.model.gamemodel.command.DrawEnvelopeCommand;

import java.io.Serializable;

/**
 * Created by usuario-pc on 12/12/2015.
 */
public class Envelope  extends SpecialLand implements Serializable{

    public Envelope() {
        setConstructionBehavior(ConstructionAllowance.CONSTRUCTION_DENIED);
        setAssignment(new DrawEnvelopeCommand());
    }

    @Override
    public String getName() {
        return "Envelope";
    }
}
