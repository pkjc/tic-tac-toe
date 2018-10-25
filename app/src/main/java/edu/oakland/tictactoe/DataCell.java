package edu.oakland.tictactoe;

import java.util.Observable;

public class DataCell extends Observable {
    public void setSymbol(String symbol){
        setChanged();
        notifyObservers(symbol);
    }
}
