package edu.oakland.tictactoe;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class Settings1Activity extends AppCompatActivity {
    private EditText pl1Name;
    private Button continueButton;
    private Button inviteBtn;
    private EditText phoneNumberText;
    private RadioGroup pl1RadioGroup;
    private RadioButton pl1RadioButton;

    SmsManager smsManager = SmsManager.getDefault();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings1);

        pl1RadioGroup = findViewById(R.id.pl1RadioGroup);
        continueButton = findViewById(R.id.continueButton);
        phoneNumberText = findViewById(R.id.phoneNumber);
        inviteBtn = findViewById(R.id.inviteButton);

        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // get selected radio button from radioGroup
                int selectedId = pl1RadioGroup.getCheckedRadioButtonId();
                String selectedSym = null;
                pl1Name = findViewById(R.id.pl1Name);

                if(selectedId == R.id.radio1){
                    selectedSym = "o";
                }else {
                    selectedSym = "x";
                }

                Intent intent = new Intent(Settings1Activity.this, Settings2Activity.class);
                intent.putExtra("player1name", pl1Name.getText().toString());
                intent.putExtra("player1symbol", selectedSym);

                startActivity(intent);
            }
        });

        inviteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(Settings1Activity.this, "Invite btn clicked", Toast.LENGTH_LONG).show();

                // get selected radio button from radioGroup
                int selectedId = pl1RadioGroup.getCheckedRadioButtonId();
                String selectedSym = null;
                pl1Name = findViewById(R.id.pl1Name);

                if(selectedId == R.id.radio1){
                    selectedSym = "o";
                }else {
                    selectedSym = "x";
                }
                //Encode message
                StringBuilder sb = new StringBuilder();
                sb.append("STicTacToe").append(" ").append("TIC-TAC-TOE").append(" ").append("INVITE").
                        append(" ").append(pl1Name.getText().toString()).append(" ").append(selectedSym);
                String phoneNumber = phoneNumberText.getText().toString();
                smsManager.sendTextMessage(phoneNumber, null, sb.toString(), null, null);
                Toast.makeText(Settings1Activity.this, sb, Toast.LENGTH_LONG).show();
            }
        });

        if(!PermissionsUtil.isSmsPermissionGranted(this)) {
            PermissionsUtil.requestReadAndSendSmsPermission(this);
        }
    }
}
