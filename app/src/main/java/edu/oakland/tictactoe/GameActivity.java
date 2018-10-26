package edu.oakland.tictactoe;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class GameActivity extends AppCompatActivity implements View.OnClickListener {
    GameButton gameButton = null;
    TextView playerName = null;
    Button startBtn = null;
    Button cancelBtn = null;
    Button resetBtn = null;

    List<GameButton> gameBtns = new ArrayList<GameButton>();
    GridLayout gameBoard = null;
    List<DataCell> dataCells = new ArrayList<DataCell>();
    Player curPlayer = null;
    String curPlayerName = null;
    String curPlayerSymbol = null;
    Player otherPlayer = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_board);

        // gameButton = findViewById(R.id.button1);
        gameBoard = findViewById(R.id.gameBoard);
        playerName = findViewById(R.id.playername);
        //playerName.setText("Rashmi Pethe");

        /*for(int i = 0; i < 9; i++){
            GameButton gameBtn = new GameButton(this.getApplicationContext());
            gameBtn.setBtnIndex(i);

         //   gameBtn.setBackgroundColor(i % 2 == 0 ? Color.BLUE: Color.CYAN);
            gameBoard.addView(gameBtn, i);
        }*/

        startBtn = findViewById(R.id.start);
        cancelBtn = findViewById(R.id.cancel);
        resetBtn = findViewById(R.id.reset);
        for (int i = 0; i < 9; i++) {
            DataCell dataCell = new DataCell();
            GameButton btn = (GameButton) gameBoard.getChildAt(i);
            btn.setActivated(false);
            dataCell.addObserver(btn);
            dataCells.add(dataCell);
            gameBtns.add(btn);
        }

        for(GameButton b: gameBtns){
            b.setOnClickListener(this);
        }
        startBtn.setOnClickListener(this);
        cancelBtn.setOnClickListener(this);
        resetBtn.setOnClickListener(this);

        Intent intent = getIntent();
        Player player1 = (Player) intent.getSerializableExtra("player1");
        Player player2 = (Player) intent.getSerializableExtra("player2");

        if(player1.isCurrentPlayer()){
            curPlayer = player1;
            otherPlayer = player2;
            curPlayerName = player1.getName();
            curPlayerSymbol = player1.getSymbol();

            playerName.setText(curPlayerName);
        }
    }

    @Override
    public void onClick(View v) {
        if(startBtn.equals(v.getId())){
            //enable all the buttons
        } else if(cancelBtn.equals(v.getId())){
            //disable all buttons
        } else if(resetBtn.equals(v.getId())){
            //reset the game
        } else {
            //game button click
            int index = ((GameButton)v).getBtnIndex();
            //call datacell
            curPlayer.markCell(dataCells.get(index), index);
            //Set the button with player image
            if (curPlayerSymbol.equals("o")){
                gameBtns.get(index).setBackgroundResource(R.drawable.o);
            } else{
               gameBtns.get(index).setBackgroundResource(R.drawable.x);
            }
            swapPlayers();
        }
    }

    public void swapPlayers(){
        Player temp = null;
        temp = curPlayer;
        curPlayer = otherPlayer;
        otherPlayer = temp;
    }
}
