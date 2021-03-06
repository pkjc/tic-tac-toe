package edu.oakland.tictactoe;

import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
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
    //Button editBtn;
    //Timer
    Chronometer timer = null;
    Boolean resume = false;
    long elapsedTime;

    GameButton[] gameBtns = new GameButton[9];
    DataCell[] dataCells = null;
    HashMap<String, Player> playersMap = new HashMap<String, Player>();

    GridLayout gameBoard;
    Player player1;
    Player player2;
    Boolean isFirstMove = false;
    String destPhone = null;
    Player instanceOwner;

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
        instanceOwner = (Player)intent.getSerializableExtra("instanceOwner");

        playersMap.put(player1.getSymbol(), player1);
        playersMap.put(player2.getSymbol(), player2);

        gameBoard = findViewById(R.id.gameBoard);
        playerName = findViewById(R.id.playername);
        playerNameLabel = findViewById(R.id.player);
        startBtn = findViewById(R.id.start);
        cancelBtn = findViewById(R.id.cancel);
        resetBtn = findViewById(R.id.reset);
        timer = findViewById(R.id.timer);

        timer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer chronometer) {
                if(!resume){
                    long minutes = ((SystemClock.elapsedRealtime() - timer.getBase())/1000) / 60;
                    long seconds = ((SystemClock.elapsedRealtime() - timer.getBase())/1000) % 60;
                    elapsedTime = SystemClock.elapsedRealtime();
                } else {
                    long minutes = ((elapsedTime - timer.getBase())/1000) / 60;
                    long seconds = ((elapsedTime - timer.getBase())/1000) % 60;
                    elapsedTime = elapsedTime + 1000;
                }
            }
        });

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
            resetTimer();
        }
        return win;
    }

    private void resetTimer(){
        //Stop the timer as move sms arrived
        timer.stop();
        timer.setBase(SystemClock.elapsedRealtime());
        timer.setText("00:00");
    }

    private void startTimer(){
        timer.setBase(SystemClock.elapsedRealtime());
        timer.start();
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
            resetTimer();
            //playerName.setText(player1.getName());
            playerName.setText(instanceOwner.getName());
            if(player1.getName().equalsIgnoreCase(instanceOwner.getName())){
                player1.setCurrentPlayer(true);
                player2.setCurrentPlayer(false);
            } else {
                player2.setCurrentPlayer(true);
                player1.setCurrentPlayer(false);
            }
            for (GameButton b : gameBtns) {
                b.setBackgroundResource(R.drawable.radio_bg);
                b.setEnabled(true);
                String encodedText = ApplicationUtil.encodeTextSMS(instanceOwner.getName(), instanceOwner.getSymbol(), "START", 0);
                smsManager.sendTextMessage(destPhone, null, encodedText, null, null);
            }
        } else if (cancelBtn.getId() == v.getId()) {
            resetTimer();
            stopGame();
            startBtn.setEnabled(true);
            //Send cancel sms to player2
            String encodedText = ApplicationUtil.encodeTextSMS(instanceOwner.getName(), instanceOwner.getSymbol(), "CANCEL", 0);
            smsManager.sendTextMessage(destPhone, null, encodedText, null, null);
        } else if (resetBtn.getId() == v.getId()) {
            stopGame();
            startBtn.setEnabled(true);
            //Send reset sms to player2
            String encodedText = ApplicationUtil.encodeTextSMS(instanceOwner.getName(), instanceOwner.getSymbol(), "RESET", 0);
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
                    smsManager.sendTextMessage(destPhone, null, encodedText, null, null);
                }
                if(isFirstMove) {
                    startBtn.setEnabled(false);
                    isFirstMove = false;
                }
                //Start the timer until the move sms arrives
                if(!win) {
                    startTimer();
                }

                //for (GameButton b : gameBtns) {
                for(int i=0; i < gameBtns.length; i++){
                    gameBtns[i].setEnabled(false);
                    //Toast.makeText(this, b.getTag().toString(), Toast.LENGTH_SHORT).show();
                    if(!gameBtns[i].getTag().equals(player1.getSymbol()) && !gameBtns[i].getTag().equals(player2.getSymbol())){
                        gameBtns[i].setBackgroundResource(R.drawable.rounded_rect_filled);
                    }
                }
                gameBtns[index].setEnabled(false);
            }
        }
    }

    /*
     * This method processes the Move Request
     */
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
        //Stop the timer as move sms arrived
        resetTimer();

        gameBtns[dataCell].setEnabled(false);

        for(int i=0; i < gameBtns.length ; i++){
            if(!gameBtns[i].getTag().equals(player1.getSymbol()) && !gameBtns[i].getTag().equals(player2.getSymbol())){
                gameBtns[i].setEnabled(true);
                gameBtns[i].setBackgroundResource(R.drawable.radio_bg);
            }
            //Toast.makeText(this, "PLAYED", Toast.LENGTH_LONG).show();
            //Log.d("<-- GAME ACTIVITY --> ", gameBtns[i].getTag().toString() + " DATACELL " + dataCell);
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

    /*
     * This method processes the Win Scenario Request
     */
    public void processWinRequest(String plName, String plSymbol, String senderNum, int dataCell){
        //Toast.makeText(getApplicationContext(), "Win action"+playerName, Toast.LENGTH_SHORT).show();
        Player wonPlayer = playersMap.get(plSymbol);

        //Update the last move
        wonPlayer.markCell(dataCells[dataCell], dataCell);
        gameBtns[dataCell].setTag(plSymbol);

        playerName.setText(wonPlayer.getName() + " has won!");
        playerName.setTextSize(24);
        playerName.setTextColor(Color.RED);
        playerNameLabel.setText("");
        for (GameButton b : gameBtns) {
            b.setEnabled(false);
        }
        //Stop the timer as win sms arrived
        resetTimer();
    }

    /*
    * This method processes the Cancel Request
    */
    public void processCancelRequest(String plName, String plSymbol, String senderNum, int dataCell){
        stopGame();
        startBtn.setEnabled(true);
        Toast.makeText(this, "Cancel request from " + plName + ", resetting the Game.", Toast.LENGTH_LONG).show();
    }

    /*
     * This method processes the Reset Request
     */
    public void processResetRequest(String plName, String plSymbol, String senderNum, int dataCell){
        stopGame();
        startBtn.setEnabled(true);
        Toast.makeText(this, "Reset request from " + plName + ", resetting the Game.", Toast.LENGTH_LONG).show();
    }

    /*
     * This method processes the Start Request
     */
    public void processStartRequest(String plName, String playerSymbol, String senderNum) {
        startBtn.setEnabled(false);
        playerName.setText(plName);
        for (GameButton b : gameBtns) {
            b.setBackgroundResource(R.drawable.radio_bg);
        }
    }
}