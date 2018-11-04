package edu.oakland.tictactoe;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class Settings2Activity extends AppCompatActivity {
    private EditText pl2Name;
    private Button submitBtn;
    private RadioGroup pl2RadioGroup;
    private RadioButton pl2RadioButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings2);
        pl2RadioGroup = findViewById(R.id.pl2RadioGroup);

        Intent intent = getIntent();

        final String player1name = intent.getStringExtra("player1name");
        final String player1symbol = intent.getStringExtra("player1symbol");

        submitBtn = findViewById(R.id.player2NameSubmit);
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // get selected radio button from radioGroup
                int selectedId = pl2RadioGroup.getCheckedRadioButtonId();
                String selectedSym = null;
                pl2Name = findViewById(R.id.pl2Name);

                if(selectedId == R.id.radio1){
                    selectedSym = "o";
                }else {
                    selectedSym = "x";
                }

                Intent intent = new Intent(Settings2Activity.this, GameActivity.class);
                Player player1 = new Player(player1name, player1symbol, true);
                Player player2 = new Player(pl2Name.getText().toString(), selectedSym, false);
                intent.putExtra("player1", player1);
                intent.putExtra("player2", player2);
                startActivity(intent);
            }
        });
    }
}
