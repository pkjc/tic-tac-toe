package edu.oakland.tictactoe;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;

public class GameActivity extends AppCompatActivity implements View.OnClickListener {
    TextView playerName;
    Button startBtn;
    Button cancelBtn;
    Button resetBtn;

    GameButton[] gameBtns = new GameButton[9];
    DataCell[] dataCells = new DataCell[9];

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
        startBtn = findViewById(R.id.start);
        cancelBtn = findViewById(R.id.cancel);
        resetBtn = findViewById(R.id.reset);

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
        for (GameButton b : gameBtns) {
            b.setBackgroundResource(R.drawable.rounded_rect_filled);
            b.setEnabled(false);
        }
        playerName.setText("");
    }

    private void playerWonProcess(Player player) {
        String symbol = player.getSymbol();
        if(winningConditions(symbol)){
            playerName.setText(player.getName() + " has won!");
            stopGame();
        }
    }

    private boolean winningConditions(String symbol) {
        Bitmap playerSymbol = null;
        if (symbol.equalsIgnoreCase("o")) {
            playerSymbol = BitmapFactory.decodeResource(getBaseContext().getResources(), R.drawable.o);
        } else if (symbol.equalsIgnoreCase("x")) {
            playerSymbol = BitmapFactory.decodeResource(getBaseContext().getResources(), R.drawable.x);
        }
        BitmapDrawable[] btnBg = new BitmapDrawable[9];
        for (int i = 0; i < gameBtns.length; i++) {
            //btnBg[i] = (BitmapDrawable)gameBtns[i].getBackground().getCurrent();
        }
        //Toast.makeText(this, gameBtns[0].getBackground().equals(getResources().getDrawable(R.drawable.o)) + " ---- ", Toast.LENGTH_LONG).show();
        return false;
//        return btnBg[0].getBitmap().sameAs(playerSymbol) &&
//                btnBg[1].getBitmap().sameAs(playerSymbol) &&
//                btnBg[2].getBitmap().sameAs(playerSymbol) ||
//                btnBg[3].getBitmap().sameAs(playerSymbol) &&
//                        btnBg[4].getBitmap().sameAs(playerSymbol) &&
//                        btnBg[5].getBitmap().sameAs(playerSymbol) ||
//                btnBg[6].getBitmap().sameAs(playerSymbol) &&
//                        btnBg[7].getBitmap().sameAs(playerSymbol) &&
//                        btnBg[8].getBitmap().sameAs(playerSymbol) ||
//                btnBg[0].getBitmap().sameAs(playerSymbol) &&
//                        btnBg[3].getBitmap().sameAs(playerSymbol) &&
//                        btnBg[6].getBitmap().sameAs(playerSymbol) ||
//                btnBg[1].getBitmap().sameAs(playerSymbol) &&
//                        btnBg[4].getBitmap().sameAs(playerSymbol) &&
//                        btnBg[7].getBitmap().sameAs(playerSymbol) ||
//                btnBg[2].getBitmap().sameAs(playerSymbol) &&
//                        btnBg[5].getBitmap().sameAs(playerSymbol) &&
//                        btnBg[8].getBitmap().sameAs(playerSymbol) ||
//                btnBg[0].getBitmap().sameAs(playerSymbol) &&
//                        btnBg[4].getBitmap().sameAs(playerSymbol) &&
//                        btnBg[8].getBitmap().sameAs(playerSymbol) ||
//                btnBg[2].getBitmap().sameAs(playerSymbol) &&
//                        btnBg[4].getBitmap().sameAs(playerSymbol) &&
//                        btnBg[6].getBitmap().sameAs(playerSymbol);
    }

    @Override
    public void onClick(View v) {
        if (startBtn.getId() == v.getId()) {
            //enable all the buttons
            for (GameButton b : gameBtns) {
                b.setBackgroundResource(R.drawable.radio_bg);
                b.setEnabled(true);
            }
        } else if (cancelBtn.getId() == v.getId()) {
            //disable all buttons
            stopGame();
        } else if (resetBtn.getId() == v.getId()) {
            //reset the game
            stopGame();
        } else {
            //game button click
            if (v instanceof GameButton) {
                int index = ((GameButton) v).getBtnIndex();
                if (player1.isCurrentPlayer()) {
                    Toast.makeText(this, ((GameButton) v).getBackground().equals(getResources().getDrawable(R.drawable.o)) + " ---- ", Toast.LENGTH_LONG).show();
                    player1.markCell(dataCells[index], index);
                    player1.setCurrentPlayer(false);
                    player2.setCurrentPlayer(true);
                    playerName.setText(player1.getName());
                    playerWonProcess(player1);
                } else {
                    //Toast.makeText(GameActivity.this, "player2 " + player2.getSymbol(), Toast.LENGTH_SHORT).show();
                    player2.markCell(dataCells[index], index);
                    player2.setCurrentPlayer(false);
                    player1.setCurrentPlayer(true);
                    playerName.setText(player2.getName());
                    playerWonProcess(player2);
                }
                gameBtns[index].setEnabled(false);
            }
        }
    }
}