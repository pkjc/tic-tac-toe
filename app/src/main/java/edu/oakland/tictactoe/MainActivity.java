package edu.oakland.tictactoe;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private Button continueBtn;
    private Button yesButton, noButton;
    private TextView inviteMessage, choice;
    SmsReceiver smsReceiver = null;

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
                startActivity(intent);
            }
        });

        noButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    public void processRequest(String playerName, String playerSymbol) {
        //Enable all buttons
        choice.setEnabled(true);
        yesButton.setEnabled(true);
        noButton.setEnabled(true);

        inviteMessage.setEnabled(true);
        inviteMessage.setText("You have been invited by "+ playerName + " to play Tic-Tac-Toe. Do you want to accept this invitation?");
    }
}
