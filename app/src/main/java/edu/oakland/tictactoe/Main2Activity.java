package edu.oakland.tictactoe;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class Main2Activity extends AppCompatActivity {
    private EditText pl2Name;
    private Button submitBtn;
    private RadioGroup pl2RadioGroup;
    private RadioButton pl2RadioButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        Intent intent = getIntent();

        final String player1name = intent.getStringExtra("player1name");
        final String player1symbol = intent.getStringExtra("player1symbol");

        Toast.makeText(Main2Activity.this, player1name, Toast.LENGTH_SHORT).show();
        Toast.makeText(Main2Activity.this, player1symbol, Toast.LENGTH_LONG).show();

        submitBtn = findViewById(R.id.player1NameSubmit);
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // get selected radio button from radioGroup
                int selectedId = pl2RadioGroup.getCheckedRadioButtonId();
                String selectedSym = null;
                pl2Name = findViewById(R.id.pl1Name);

                if(selectedId == R.id.radio1){
                    selectedSym = "o";
                }else {
                    selectedSym = "x";
                }

                //TODO
                Intent intent = new Intent(Main2Activity.this, GameActivity.class);

//                intent.putExtra("player1name", player1name);
//                intent.putExtra("player1symbol", player1symbol);
                intent.putExtra("player2name", pl2Name.getText().toString());
                intent.putExtra("player2symbol", selectedSym);
                Player player1 = new Player(player1name, player1symbol, false);
                intent.putExtra("player1", player1);
                startActivity(intent);
            }
        });
    }
}
