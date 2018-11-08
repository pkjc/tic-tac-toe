package edu.oakland.tictactoe;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class Settings2Activity extends AppCompatActivity {
    private EditText pl2Name;
    private Button continueBtn;
    private RadioGroup pl2RadioGroup;
    private RadioButton pl2RadioButton;
    private String pl2Symbol;
    private String pl1Name;
    private String pl1Symbol;

    SmsManager smsManager = SmsManager.getDefault();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings2);
        pl2RadioGroup = findViewById(R.id.pl2RadioGroup);
        continueBtn = findViewById(R.id.player2Continue);
        Intent intent = getIntent();

        pl1Name = intent.getStringExtra("player1name");
        pl1Symbol = intent.getStringExtra("player1symbol");

        continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // get selected radio button from radioGroup 650 555-6789
                int selectedId = pl2RadioGroup.getCheckedRadioButtonId();
                //String selectedSym = null;
                pl2Name = findViewById(R.id.pl2Name);

                if(selectedId == R.id.radio1){
                    pl2Symbol = "o";
                }else {
                    pl2Symbol = "x";
                }

                //Choice is Yes and player 2 enters details, communicate this to player1.
                String encodedText = ApplicationUtil.encodeTextSMS(pl2Name.getText().toString(), pl2Symbol, "ACCEPT");
                String phoneNumber = "5554";
                smsManager.sendTextMessage(phoneNumber, null, encodedText, null, null);
                Toast.makeText(Settings2Activity.this, encodedText, Toast.LENGTH_LONG).show();
                //Navigate to Game screen
                Intent intent = new Intent(Settings2Activity.this, GameActivity.class);
                Player player1 = new Player(pl1Name, pl1Symbol, false);
                Player player2 = new Player(pl2Name.getText().toString(), pl2Symbol, true);
                intent.putExtra("player1", player1);
                intent.putExtra("player2", player2);
                startActivity(intent);
            }
        });

        if(!PermissionsUtil.isSmsPermissionGranted(this)) {
            PermissionsUtil.requestReadAndSendSmsPermission(this);
        }
    }
}
