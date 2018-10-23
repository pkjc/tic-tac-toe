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

public class MainActivity extends AppCompatActivity {

    private EditText pl1Name;
    private Button submitBtn;
    private RadioGroup pl1RadioGroup;
    private RadioButton pl1RadioButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pl1RadioGroup = findViewById(R.id.pl1RadioGroup);

        submitBtn = findViewById(R.id.player1NameSubmit);
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // get selected radio button from radioGroup
                int selectedId = pl1RadioGroup.getCheckedRadioButtonId();
                String selectedSym = null;
                pl1Name = findViewById(R.id.pl1Name);

                if(selectedId == R.id.radio1){
                    selectedSym = "o";
                }else {
                    selectedSym = "x";
                }

                Toast.makeText(MainActivity.this, selectedSym, Toast.LENGTH_LONG).show();
                Intent intent = new Intent(MainActivity.this, Main2Activity.class);
                intent.putExtra("player1name", pl1Name.getText().toString());
                intent.putExtra("player1symbol", selectedSym);

                startActivity(intent);
            }
        });
    }
}
