package edu.oakland.tictactoe;

import android.content.Intent;
import android.content.IntentFilter;
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
    private String pl1Symbol;
    private String pl2Name;
    private String pl2Symbol;
    private String senderNumber;

    SmsManager smsManager = SmsManager.getDefault();
    SmsReceiver smsReceiver = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings1);

        pl1RadioGroup = findViewById(R.id.pl1RadioGroup);
        continueButton = findViewById(R.id.player1Continue);
        phoneNumberText = findViewById(R.id.phoneNumber);
        inviteBtn = findViewById(R.id.inviteButton);

        smsReceiver = new SmsReceiver(Settings1Activity.this);
        registerReceiver(smsReceiver, new IntentFilter("android.provider.Telephony.SMS_RECEIVED"));

        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // get selected radio button from radioGroup
                int selectedId = pl1RadioGroup.getCheckedRadioButtonId();
                pl1Name = findViewById(R.id.pl1Name);

                if(selectedId == R.id.radio1){
                    pl1Symbol = "o";
                }else {
                    pl1Symbol = "x";
                }

                Intent intent = new Intent(Settings1Activity.this, GameActivity.class);
                Player player1 = new Player(pl1Name.getText().toString(), pl1Symbol, true);
                Player player2 = new Player(pl2Name, pl2Symbol, false);
                intent.putExtra("player1", player1);
                intent.putExtra("player2", player2);
                intent.putExtra("firstMove", true);
                intent.putExtra("destPhone", senderNumber);
                startActivity(intent);
            }
        });

        inviteBtn.setOnClickListener(new View.OnClickListener() {
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
                String encodedText = ApplicationUtil.encodeTextSMS(pl1Name.getText().toString(), selectedSym, "INVITE", 0);
                String phoneNumber = phoneNumberText.getText().toString();
                smsManager.sendTextMessage(phoneNumber, null, encodedText, null, null);
                //Toast.makeText(Settings1Activity.this, encodedText, Toast.LENGTH_LONG).show();

                continueButton.setEnabled(false);
            }
        });

        if(!PermissionsUtil.isSmsPermissionGranted(this)) {
            PermissionsUtil.requestReadAndSendSmsPermission(this);
        }
    }

    public void processAcceptRequest(String playerName, String playerSymbol, String srcPhoneNumber){
        continueButton.setEnabled(true);
        pl2Name = playerName;
        pl2Symbol = playerSymbol;
        senderNumber = srcPhoneNumber;
        Toast.makeText(this, "INVITE accepted. Click CONTINUE to proceed.", Toast.LENGTH_LONG).show();
    }

    public void processDeclineRequest(String playerName, String playerSymbol, String srcPhoneNumber){
        Toast.makeText(this, "Invite DECLINED by " + srcPhoneNumber, Toast.LENGTH_LONG).show();
        continueButton.setEnabled(true);
    }
}
