package edu.oakland.tictactoe;

import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

public class GameActivity extends AppCompatActivity implements View.OnClickListener {
    TextView playerName;
    TextView playerNameLabel;
    Button startBtn;
    Button cancelBtn;
    Button resetBtn;

    GameButton[] gameBtns = new GameButton[9];
    DataCell[] dataCells = null;
    HashMap<String, Player> playersMap = new HashMap<String, Player>();

    GridLayout gameBoard;
    Player player1;
    Player player2;
    Boolean isFirstMove = false;
    String destPhone = null;

    SmsManager smsManager = SmsManager.getDefault();
    SmsReceiver smsReceiver = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_board);

        smsReceiver = new SmsReceiver(GameActivity.this);
        registerReceiver(smsReceiver, new IntentFilter("android.provider.Telephony.SMS_RECEIVED"));

        Intent intent = getIntent();
        player1 = (Player) intent.getSerializableExtra("player1");
        player2 = (Player) intent.getSerializableExtra("player2");
        destPhone = intent.getStringExtra("destPhone");

        playersMap.put(player1.getSymbol(), player1);
        playersMap.put(player2.getSymbol(), player2);

        gameBoard = findViewById(R.id.gameBoard);
        playerName = findViewById(R.id.playername);
        playerNameLabel = findViewById(R.id.player);
        startBtn = findViewById(R.id.start);
        cancelBtn = findViewById(R.id.cancel);
        resetBtn = findViewById(R.id.reset);

        initGame();
        //If first move, then enable cancel and reset buttons of player2.
        // Disable start button of player1
        if(intent.getBooleanExtra("firstMove",false)){
           isFirstMove = true;
        }
        //Disable the start, cancel, reset buttons for Player2
        if(intent.getBooleanExtra("disableStart",false)) {
            startBtn.setEnabled(false);
            cancelBtn.setEnabled(false);
            resetBtn.setEnabled(false);
        }
    }

    private void initGame() {
        dataCells = new DataCell[9];
        for (int i = 0; i < 9; i++) {
            GameButton btn = (GameButton) gameBoard.getChildAt(i);
            int index = btn.getBtnIndex();
            dataCells[index] = new DataCell();
            dataCells[index].addObserver(btn);
            gameBtns[index] = btn;
            gameBtns[index].setOnClickListener(this);
            btn.setEnabled(false);
        }
        player1.setCurrentPlayer(true);
        playerName.setText(player1.getName());
        startBtn.setOnClickListener(this);
        cancelBtn.setOnClickListener(this);
        resetBtn.setOnClickListener(this);
    }

    private void stopGame() {
        dataCells = null;
        initGame();
        for (GameButton b : gameBtns) {
            b.setBackgroundResource(R.drawable.rounded_rect_filled);
            b.setTag("");
            b.setEnabled(false);
        }
        playerName.setText("");
        playerNameLabel.setText("Current Player: ");
        playerName.setTextSize(18);
        playerName.setTextColor(Color.BLACK);
    }

    private boolean playerWonProcess(Player player) {
        String symbol = player.getSymbol();
        boolean win = false;
        if(winningConditions(symbol)){
            playerName.setText(player.getName() + " has won!");
            playerName.setTextSize(32);
            playerName.setTextColor(Color.RED);
            playerNameLabel.setText("");
            for (GameButton b : gameBtns) {
                b.setEnabled(false);
            }
            win = true;
        }
        return win;
    }

    private boolean winningConditions(String playerSymbol) {
        return gameBtns[0].getTag().equals(playerSymbol) &&
                gameBtns[1].getTag().equals(playerSymbol) &&
                gameBtns[2].getTag().equals(playerSymbol) ||
                gameBtns[3].getTag().equals(playerSymbol) &&
                        gameBtns[4].getTag().equals(playerSymbol) &&
                        gameBtns[5].getTag().equals(playerSymbol) ||
                gameBtns[6].getTag().equals(playerSymbol) &&
                        gameBtns[7].getTag().equals(playerSymbol) &&
                        gameBtns[8].getTag().equals(playerSymbol) ||
                gameBtns[0].getTag().equals(playerSymbol) &&
                        gameBtns[3].getTag().equals(playerSymbol) &&
                        gameBtns[6].getTag().equals(playerSymbol) ||
                gameBtns[1].getTag().equals(playerSymbol) &&
                        gameBtns[4].getTag().equals(playerSymbol) &&
                        gameBtns[7].getTag().equals(playerSymbol) ||
                gameBtns[2].getTag().equals(playerSymbol) &&
                        gameBtns[5].getTag().equals(playerSymbol) &&
                        gameBtns[8].getTag().equals(playerSymbol) ||
                gameBtns[0].getTag().equals(playerSymbol) &&
                        gameBtns[4].getTag().equals(playerSymbol) &&
                        gameBtns[8].getTag().equals(playerSymbol) ||
                gameBtns[2].getTag().equals(playerSymbol) &&
                        gameBtns[4].getTag().equals(playerSymbol) &&
                        gameBtns[6].getTag().equals(playerSymbol);
    }

    @Override
    public void onClick(View v) {
        if (startBtn.getId() == v.getId()) {
            playerName.setText(player1.getName());
            for (GameButton b : gameBtns) {
                b.setBackgroundResource(R.drawable.radio_bg);
                b.setEnabled(true);
            }
        } else if (cancelBtn.getId() == v.getId()) {
            stopGame();
            startBtn.setEnabled(true);
            //Send cancel sms to player2
            String encodedText = ApplicationUtil.encodeTextSMS(player1.getName(), player1.getSymbol(), "CANCEL", 0);
            smsManager.sendTextMessage(destPhone, null, encodedText, null, null);
        } else if (resetBtn.getId() == v.getId()) {
            stopGame();
            startBtn.setEnabled(true);
            //Send reset sms to player2
            String encodedText = ApplicationUtil.encodeTextSMS(player1.getName(), player1.getSymbol(), "RESET", 0);
            smsManager.sendTextMessage(destPhone, null, encodedText, null, null);
        } else {
            if (v instanceof GameButton) {
                int index = ((GameButton) v).getBtnIndex();
                boolean win = false;
                if (player1.isCurrentPlayer()) {
                    player1.markCell(dataCells[index], index);
                    player1.setCurrentPlayer(false);
                    player2.setCurrentPlayer(true);
                    playerName.setText(player2.getName());
                    ((GameButton) v).setTag(player1.getSymbol());
                    win = playerWonProcess(player1);
                    //If not game over, send message to player 2
                    String action = "";
                    if (win){
                        action = "WIN";
                    } else {
                        action = "MOVE";
                    }
                    String encodedText = ApplicationUtil.encodeTextSMS(player1.getName(), player1.getSymbol(), action, index);
                    //String phoneNumber = "5554";
                    smsManager.sendTextMessage(destPhone, null, encodedText, null, null);

                } else {
                    player2.markCell(dataCells[index], index);
                    player2.setCurrentPlayer(false);
                    player1.setCurrentPlayer(true);
                    playerName.setText(player1.getName());
                    ((GameButton) v).setTag(player2.getSymbol());
                    win = playerWonProcess(player2);
                    //If not game over, send message to player 1
                    String action = "";
                    if (win){
                        action = "WIN";
                    } else {
                        action = "MOVE";
                    }
                    String encodedText = ApplicationUtil.encodeTextSMS(player2.getName(), player2.getSymbol(), action, index);
                    //String phoneNumber = "5556";
                    smsManager.sendTextMessage(destPhone, null, encodedText, null, null);
                }
                if(isFirstMove) {
                    startBtn.setEnabled(false);
                    isFirstMove = false;
                }
                for (GameButton b : gameBtns) {
                    b.setEnabled(false);
                    // Tags are indexes eg 0, 1, 2.. before they're set to o or x
                    if(b.getBtnIndex() != index && "".equals(b.getTag())){
                        b.setBackgroundResource(R.drawable.rounded_rect_filled);
                    }
                }
                gameBtns[index].setEnabled(false);
            }
        }
    }

    public void processMoveRequest(String plName, String plSymbol, String senderNum, int dataCell) {
        Toast.makeText(getApplicationContext(), "Move action" + plName, Toast.LENGTH_SHORT).show();
        Player movedPlayer = null;
        Player nextPlayer = null;
        //Update destPhone to senderNum to send next move
        destPhone = senderNum;
        //If first move of player1, then enable all buttons except selection for player2 to choose.
        if(isFirstMove){
            //Inside player2 turn
            startBtn.setEnabled(false);
            cancelBtn.setEnabled(true);
            resetBtn.setEnabled(true);
            isFirstMove = false;
        }

        gameBtns[dataCell].setEnabled(false);

        for (GameButton b : gameBtns) {
            b.setEnabled(true);
            //b.setBackgroundResource(R.drawable.radio_bg);
            Log.d("<-- GAME ACTIVITY --> ", b.getTag().toString() + " DATACELL " + dataCell);
            // from log: <-- GAME ACTIVITY -->: o DATACELL 2
            // from log: <-- GAME ACTIVITY -->: x DATACELL 2
            // datacell is the index of button marked
            // button tags are indexes i.e 0, 1, 2.. before they're set to 'o' or 'x'
            if(b.getBtnIndex() != dataCell && (b.getTag().toString() != "o" || b.getTag().toString() != "x")) {
                b.setBackgroundResource(R.drawable.radio_bg);
            }
        }

        for (String symbol: playersMap.keySet()) {
            if(symbol.equals(plSymbol)){
                movedPlayer = playersMap.get(symbol);
            } else {
                nextPlayer = playersMap.get(symbol);
            }
        }
        movedPlayer.markCell(dataCells[dataCell], dataCell);
        movedPlayer.setCurrentPlayer(false);
        gameBtns[dataCell].setTag(plSymbol);

        nextPlayer.setCurrentPlayer(true);
        playerName.setText(nextPlayer.getName());
        playerWonProcess(movedPlayer);
    }

    public void processWinRequest(String plName, String plSymbol, String senderNum, int dataCell){
        Toast.makeText(getApplicationContext(), "Win action"+playerName, Toast.LENGTH_SHORT).show();
        Player wonPlayer = playersMap.get(plSymbol);

        playerName.setText(wonPlayer.getName() + " has won!");
        playerName.setTextSize(24);
        playerName.setTextColor(Color.RED);
        playerNameLabel.setText("");
        for (GameButton b : gameBtns) {
            b.setEnabled(false);
        }
    }

    public void processCancelRequest(String plName, String plSymbol, String senderNum, int dataCell){
        stopGame();
        startBtn.setEnabled(true);
        Toast.makeText(this, "Cancel request from " + plName + ", resetting the Game.", Toast.LENGTH_LONG).show();
    }

    public void processResetRequest(String plName, String plSymbol, String senderNum, int dataCell){
        stopGame();
        startBtn.setEnabled(true);
        Toast.makeText(this, "Reset request from " + plName + ", resetting the Game.", Toast.LENGTH_LONG).show();
    }
}