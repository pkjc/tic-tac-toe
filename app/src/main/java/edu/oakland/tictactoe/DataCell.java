package edu.oakland.tictactoe;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class DataCell extends Observable {
    List<Observer> observers = new ArrayList<Observer>();

    public DataCell(Observer observer) {
        this.observers.add(observer);
    }

    public void setSymbol(String symbol){
        setChanged();
        for (Observer o: observers) {
            notifyObservers(symbol);
        }

    }
}
