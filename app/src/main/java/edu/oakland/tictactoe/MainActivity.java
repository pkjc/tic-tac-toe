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

    EditText pl1Name;
    Button submitBtn;
    private RadioGroup pl1RadioGroup;
    private RadioButton pl1RadioButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pl1RadioGroup = (RadioGroup) findViewById(R.id.pl1RadioGroup);

        submitBtn = findViewById(R.id.player1NameSubmit);
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // get selected radio button from radioGroup
                int selectedId = pl1RadioGroup.getCheckedRadioButtonId();

                // find the radiobutton by returned id
                pl1RadioButton = (RadioButton) findViewById(selectedId);

                pl1Name = findViewById(R.id.pl1Name);

                Toast.makeText(MainActivity.this,
                        pl1RadioButton.getText(), Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(MainActivity.this, Main2Activity.class);
                intent.putExtra("player1name", pl1Name.getText());
                intent.putExtra("player1symbol", pl1RadioButton.getText());

                startActivity(intent);
            }
        });


    }
}
