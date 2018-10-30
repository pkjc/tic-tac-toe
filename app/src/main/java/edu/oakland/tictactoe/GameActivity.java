package edu.oakland.tictactoe;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;

public class GameActivity extends AppCompatActivity implements View.OnClickListener {
    TextView playerName;
    TextView playerNameLabel;
    Button startBtn;
    Button cancelBtn;
    Button resetBtn;

    GameButton[] gameBtns = new GameButton[9];
    DataCell[] dataCells = null;

    GridLayout gameBoard;
    Player player1;
    Player player2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_board);

        Intent intent = getIntent();
        player1 = (Player) intent.getSerializableExtra("player1");
        player2 = (Player) intent.getSerializableExtra("player2");

        gameBoard = findViewById(R.id.gameBoard);
        playerName = findViewById(R.id.playername);
        playerNameLabel = findViewById(R.id.player);
        startBtn = findViewById(R.id.start);
        cancelBtn = findViewById(R.id.cancel);
        resetBtn = findViewById(R.id.reset);

        initGame();

    }

    private void initGame() {
        dataCells = new DataCell[9];
        for (int i = 0; i < 9; i++) {
            GameButton btn = (GameButton) gameBoard.getChildAt(i);
            int index = btn.getBtnIndex();
            dataCells[index] = new DataCell();
            dataCells[index].addObserver(btn);
            gameBtns[index] = btn;
            gameBtns[index].setOnClickListener(this);
            btn.setEnabled(false);
        }

        startBtn.setOnClickListener(this);
        cancelBtn.setOnClickListener(this);
        resetBtn.setOnClickListener(this);
    }

    private void stopGame() {
        dataCells = null;
        initGame();
        for (GameButton b : gameBtns) {
            b.setBackgroundResource(R.drawable.rounded_rect_filled);
            b.setTag("");
            b.setEnabled(false);
        }
        playerName.setText("");
        playerNameLabel.setText("Current Player: ");
        playerName.setTextSize(18);
        playerName.setTextColor(Color.BLACK);
    }

    private void playerWonProcess(Player player) {
        String symbol = player.getSymbol();
        if(winningConditions(symbol)){
            playerName.setText(player.getName() + " has won!");
            playerName.setTextSize(32);
            playerName.setTextColor(Color.RED);
            playerNameLabel.setText("");
            for (GameButton b : gameBtns) {
                b.setEnabled(false);
            }
        }
    }

    private boolean winningConditions(String playerSymbol) {
        return gameBtns[0].getTag().equals(playerSymbol) &&
                gameBtns[1].getTag().equals(playerSymbol) &&
                gameBtns[2].getTag().equals(playerSymbol) ||
                gameBtns[3].getTag().equals(playerSymbol) &&
                        gameBtns[4].getTag().equals(playerSymbol) &&
                        gameBtns[5].getTag().equals(playerSymbol) ||
                gameBtns[6].getTag().equals(playerSymbol) &&
                        gameBtns[7].getTag().equals(playerSymbol) &&
                        gameBtns[8].getTag().equals(playerSymbol) ||
                gameBtns[0].getTag().equals(playerSymbol) &&
                        gameBtns[3].getTag().equals(playerSymbol) &&
                        gameBtns[6].getTag().equals(playerSymbol) ||
                gameBtns[1].getTag().equals(playerSymbol) &&
                        gameBtns[4].getTag().equals(playerSymbol) &&
                        gameBtns[7].getTag().equals(playerSymbol) ||
                gameBtns[2].getTag().equals(playerSymbol) &&
                        gameBtns[5].getTag().equals(playerSymbol) &&
                        gameBtns[8].getTag().equals(playerSymbol) ||
                gameBtns[0].getTag().equals(playerSymbol) &&
                        gameBtns[4].getTag().equals(playerSymbol) &&
                        gameBtns[8].getTag().equals(playerSymbol) ||
                gameBtns[2].getTag().equals(playerSymbol) &&
                        gameBtns[4].getTag().equals(playerSymbol) &&
                        gameBtns[6].getTag().equals(playerSymbol);
    }

    @Override
    public void onClick(View v) {
        if (startBtn.getId() == v.getId()) {
            playerName.setText(player1.getName());
            for (GameButton b : gameBtns) {
                b.setBackgroundResource(R.drawable.radio_bg);
                b.setEnabled(true);
            }
        } else if (cancelBtn.getId() == v.getId()) {
            stopGame();
        } else if (resetBtn.getId() == v.getId()) {
            stopGame();
        } else {
            if (v instanceof GameButton) {
                int index = ((GameButton) v).getBtnIndex();
                if (player1.isCurrentPlayer()) {
                    player1.markCell(dataCells[index], index);
                    player1.setCurrentPlayer(false);
                    player2.setCurrentPlayer(true);
                    playerName.setText(player2.getName());
                    ((GameButton) v).setTag(player1.getSymbol());
                    playerWonProcess(player1);
                } else {
                    player2.markCell(dataCells[index], index);
                    player2.setCurrentPlayer(false);
                    player1.setCurrentPlayer(true);
                    playerName.setText(player1.getName());
                    ((GameButton) v).setTag(player2.getSymbol());
                    playerWonProcess(player2);
                }
                gameBtns[index].setEnabled(false);
            }
        }
    }
}