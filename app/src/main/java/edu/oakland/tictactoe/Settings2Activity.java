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

public class Settings2Activity extends AppCompatActivity {
    private EditText pl2Name;
    private Button continueBtn;
    private RadioGroup pl2RadioGroup;
    private RadioButton pl2RadioButton;
    private String pl2Symbol;
    private String pl1Name;
    private String pl1Symbol;
    private String player1Number;

    SmsManager smsManager = SmsManager.getDefault();
    SmsReceiver smsReceiver = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings2);
        pl2RadioGroup = findViewById(R.id.pl2RadioGroup);
        continueBtn = findViewById(R.id.player2Cont);

        Intent intent = getIntent();
        pl1Name = intent.getStringExtra("Player1Name");
        pl1Symbol = intent.getStringExtra("Player1Symbol");
        player1Number = intent.getStringExtra("Player1Number");

        RadioButton radio1 = findViewById(R.id.radio1);
        RadioButton radio2 = findViewById(R.id.radio2);
        if(pl1Symbol.equalsIgnoreCase("o")){
            radio1.setEnabled(false);
            radio1.setChecked(false);
            radio2.setChecked(true);
        }else {
            radio1.setChecked(true);
            radio2.setChecked(false);
            radio2.setEnabled(false);
        }
        //smsReceiver = new SmsReceiver(Settings2Activity.this);
        //registerReceiver(smsReceiver, new IntentFilter("android.provider.Telephony.SMS_RECEIVED"));

        continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // get selected radio button from radioGroup 650 555-6789
                int selectedId = pl2RadioGroup.getCheckedRadioButtonId();
                pl2Name = findViewById(R.id.pl2Name);

                if(pl1Symbol.equalsIgnoreCase("o")){
                    pl2Symbol = "x";
                    RadioButton rb = findViewById(selectedId);
                    rb.setEnabled(false);
                    rb.setChecked(false);
                    RadioButton rb1 = findViewById(R.id.radio2);
                    rb1.setEnabled(true);
                    rb1.setChecked(true);
                } else {
                    pl2Symbol = "o";
                }
                  //findViewById(selectedId).setEnabled(false);
                /*RadioButton radio1 = findViewById(R.id.radio1);
                RadioButton radio2 = findViewById(R.id.radio2);
                if(pl1Symbol == radio1.toString()){
                    //pl2Symbol = "o";
                    //findViewById(selectedId);
                    radio1.setEnabled(false);
                    radio1.setChecked(false);
                    radio2.setChecked(true);
                    pl2Symbol = "x";
                }else {
                    //pl2Symbol = "x";
                    radio1.setChecked(true);
                    radio2.setChecked(false);
                    radio2.setEnabled(false);
                    pl2Symbol = "o";
                }*/

                //Choice is Yes and player 2 enters details, communicate this to player1.
                String encodedText = ApplicationUtil.encodeTextSMS(pl2Name.getText().toString(), pl2Symbol, "ACCEPT", 0);
                smsManager.sendTextMessage(player1Number, null, encodedText, null, null);
                //Toast.makeText(Settings2Activity.this, encodedText, Toast.LENGTH_LONG).show();
                //Navigate to Game screen
                Intent intent = new Intent(Settings2Activity.this, GameActivity.class);
                Player player1 = new Player(pl1Name, pl1Symbol, true);
                Player player2 = new Player(pl2Name.getText().toString(), pl2Symbol, false);
                intent.putExtra("player1", player1);
                intent.putExtra("player2", player2);
                intent.putExtra("disableStart", true);
                intent.putExtra("firstMove", true);
                intent.putExtra("instanceOwner", player2);
                startActivity(intent);
            }
        });

        if(!PermissionsUtil.isSmsPermissionGranted(this)) {
            PermissionsUtil.requestReadAndSendSmsPermission(this);
        }
    }
}
