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

    List<Button> gameBtns = new ArrayList<Button>();
    GridLayout gameBoard = null;

    String curPlayerSymbol = null;
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
            Button btn = (Button) gameBoard.getChildAt(i);
            gameBtns.add(btn);
        }

        for(Button b: gameBtns){
            b.setOnClickListener(this);
        }
        startBtn.setOnClickListener(this);
        cancelBtn.setOnClickListener(this);
        resetBtn.setOnClickListener(this);

        Intent intent = getIntent();
        String playerNameVal = intent.getStringExtra("player1name");
        String playerSym = intent.getStringExtra("player1symbol");

        playerName.setText(playerNameVal);
        curPlayerSymbol = playerSym;

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
           // int index = (int) v.getTag();
            //call datacell
            //Set the button with player image
            //gameBtns.get(0).setText(curPlayerSymbol);

        }


    }
}
