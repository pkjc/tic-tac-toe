package edu.oakland.tictactoe;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

public class GameActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_board);
        Player player1 = (Player) getIntent().getSerializableExtra("player1");
        Log.d("THIS IS IT:", player1.getName());
    }
}
