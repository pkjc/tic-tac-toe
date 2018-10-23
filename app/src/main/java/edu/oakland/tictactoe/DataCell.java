package edu.oakland.tictactoe;

import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class DataCell implements Observable {
    List<Observer> observerList = null;

    public DataCell() {
        this.observerList = new ArrayList<Observer>();
    }

    @Override
    public void register(Observer observer) {
        observerList.add(observer);
    }

    @Override
    public void unregister(Observer observer) {
        observerList.remove(observer);
    }

    @Override
    public void notifyObservers() {
        View v = null;
        for (Observer o: observerList) {
            o.update(v);
        }
    }
}
