package edu.oakland.tictactoe;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

public class Main2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        Intent intent = getIntent();

        String player1name = intent.getStringExtra("player1name");
        String player1symbol = intent.getStringExtra("player1symbol");

        Toast.makeText(Main2Activity.this, player1name, Toast.LENGTH_SHORT).show();
        Toast.makeText(Main2Activity.this, player1symbol, Toast.LENGTH_LONG).show();
    }
}
