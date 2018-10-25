package edu.oakland.tictactoe;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.GridLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class GameActivity extends AppCompatActivity {
    GameButton gameButton = null;
    TextView playerName = null;
    List<GameButton> gameBtns = new ArrayList<GameButton>();
    GridLayout gameBoard = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_board);

        Player player1 = (Player) getIntent().getSerializableExtra("player1");
        Log.d("THIS IS IT:", player1.getName());

        // gameButton = findViewById(R.id.button1);
        gameBoard = findViewById(R.id.gameBoard);
        playerName = findViewById(R.id.playername);
        playerName.setText("Rashmi Pethe");

        for(int i = 0; i < 9; i++){
            GameButton gameBtn = new GameButton(this.getApplicationContext());
            gameBtn.setBtnIndex(i);
            gameBtn.setBackgroundColor(i % 2 == 0 ? Color.BLUE: Color.CYAN);
            gameBoard.addView(gameBtn, i);
        }
    }
}
