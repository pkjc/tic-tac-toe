package edu.oakland.tictactoe;

import java.io.Serializable;

public class Player implements Serializable {
	private String name;
	private DataCell dataCell[] = new DataCell[9];
	private String symbol;
	private boolean isCurrentPlayer;
	
	public Player(String name, String symbol, boolean isCurrentPlayer){
		this.name = name;
		this.symbol = symbol;
		this.isCurrentPlayer = isCurrentPlayer;
	}

	public void markCell(DataCell observable, int cellNum){
		dataCell[cellNum] = observable;
		dataCell[cellNum].setSymbol(this.symbol);
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public DataCell[] getDataCell() {
		return dataCell;
	}

	public void setDataCell(DataCell[] dataCell) {
		this.dataCell = dataCell;
	}

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public boolean isCurrentPlayer() {
		return isCurrentPlayer;
	}

	public void setCurrentPlayer(boolean isCurrentPlayer) {
		this.isCurrentPlayer = isCurrentPlayer;
	}
}