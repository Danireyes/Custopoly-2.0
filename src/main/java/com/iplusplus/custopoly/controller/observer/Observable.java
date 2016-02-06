package com.iplusplus.custopoly.controller.observer;

public interface Observable {

    void addObserver(Controller o);
    void deleteObserver(Controller o);
}
