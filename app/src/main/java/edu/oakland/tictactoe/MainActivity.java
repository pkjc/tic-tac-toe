package edu.oakland.tictactoe;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private Button continueBtn;
    private Button yesButton, noButton;
    private TextView inviteMessage, choice;
    SmsReceiver smsReceiver = null;
    String senderNumber, senderName, senderSymbol;

    SmsManager smsManager = SmsManager.getDefault();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(!PermissionsUtil.isSmsPermissionGranted(this)) {
            PermissionsUtil.requestReadAndSendSmsPermission(this);
        }

        setContentView(R.layout.activity_main);

        continueBtn = findViewById(R.id.continueBtn);
        yesButton = findViewById(R.id.yesButton);
        yesButton.setEnabled(false);
        noButton = findViewById(R.id.noButton);
        noButton.setEnabled(false);
        inviteMessage = findViewById(R.id.inviteMessage);
        inviteMessage.setEnabled(false);
        choice = findViewById(R.id.choice);
        choice.setEnabled(false);

        smsReceiver = new SmsReceiver(MainActivity.this);

        continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Settings1Activity.class);
                startActivity(intent);
            }
        });

        yesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Settings2Activity.class);
                Bundle bundle = new Bundle();
                bundle.putString("Player1Name", senderName);
                bundle.putString("Player1Symbol", senderSymbol);
                bundle.putString("Player1Number", senderNumber);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        noButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Clear up the invite message
                inviteMessage.setEnabled(false);
                inviteMessage.setText("");
                String encodedText = ApplicationUtil.encodeTextSMS(senderName, senderSymbol, "DECLINE", 0);
                smsManager.sendTextMessage(senderNumber, null, encodedText, null, null);
            }
        });
    }

    public void processInviteRequest(String playerName, String playerSymbol, String srcPhoneNumber) {
        //Enable all buttons
        choice.setEnabled(true);
        yesButton.setEnabled(true);
        noButton.setEnabled(true);

        //Set sender phone number and symbol
        senderName = playerName;
        senderSymbol = playerSymbol;
        senderNumber = srcPhoneNumber;
        inviteMessage.setEnabled(true);
        inviteMessage.setText("You have been invited by "+ playerName + " to play Tic-Tac-Toe. Do you want to accept this invitation?");
    }
}
