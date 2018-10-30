package edu.oakland.tictactoe;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;

public class GameActivity extends AppCompatActivity implements View.OnClickListener {
    TextView playerName;
    Button startBtn;
    Button cancelBtn;
    Button resetBtn;

    GameButton[] gameBtns = new GameButton[9];
    DataCell[] dataCells = new DataCell[9];

    GridLayout gameBoard;
    Player player1;
    Player player2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_board);

        Intent intent = getIntent();
        player1 = (Player) intent.getSerializableExtra("player1");
        player2 = (Player) intent.getSerializableExtra("player2");

        gameBoard = findViewById(R.id.gameBoard);
        playerName = findViewById(R.id.playername);
        startBtn = findViewById(R.id.start);
        cancelBtn = findViewById(R.id.cancel);
        resetBtn = findViewById(R.id.reset);

        for (int i = 0; i < 9; i++) {
            GameButton btn = (GameButton) gameBoard.getChildAt(i);
            int index = btn.getBtnIndex();
            dataCells[index] = new DataCell();
            dataCells[index].addObserver(btn);
            gameBtns[index] = btn;
            gameBtns[index].setOnClickListener(this);
            btn.setEnabled(false);
        }

        startBtn.setOnClickListener(this);
        cancelBtn.setOnClickListener(this);
        resetBtn.setOnClickListener(this);

    }

    private boolean winningConditions(){
        return false;
    }

    private void stopGame(){

    }

    private void resetGame(){

    }

    private void checkIfPlayerHasWon(Player player) {
        String symbol = player.getSymbol();
//        TTTButton[] btns = new TTTButton[9];
//        for(int i = 0; i< boardPanel.getComponentCount(); i++){
//            if(boardPanel.getComponent(i) instanceof TTTButton){
//                btns[i] = (TTTButton) boardPanel.getComponent(i);
//            }
//        }
//        if(winningConditions(symbol, btns)){
//            turnLabel.setText(player.getName() + " has won!");
//            turnLabel.setFont(new Font("", 1, 16));
//            stopGame(btns, true);
//        }
    }

    @Override
    public void onClick(View v) {
        if(startBtn.getId() == v.getId()){
            //enable all the buttons
            for (GameButton b: gameBtns) {
                b.setBackgroundResource(R.drawable.radio_bg);
                b.setEnabled(true);
            }
        } else if(cancelBtn.getId() == v.getId()){
            //disable all buttons
            for (GameButton b: gameBtns) {
                b.setEnabled(false);
            }
        } else if(resetBtn.getId() == v.getId()){
            //reset the game
        } else {
            //game button click
            if(v instanceof GameButton) {
                //Toast.makeText(GameActivity.this, "button clicked", Toast.LENGTH_SHORT).show();
                int index = ((GameButton) v).getBtnIndex();
                if(player1.isCurrentPlayer()){
                    //Toast.makeText(GameActivity.this, "player1 " + player1.getSymbol(), Toast.LENGTH_SHORT).show();
                    player1.markCell(dataCells[index], index);
                    player1.setCurrentPlayer(false);
                    player2.setCurrentPlayer(true);
                    // Set text in top text view
                    // Check if player has won
                }else {
                    //Toast.makeText(GameActivity.this, "player2 " + player2.getSymbol(), Toast.LENGTH_SHORT).show();
                    player2.markCell(dataCells[index], index);
                    player2.setCurrentPlayer(false);
                    player1.setCurrentPlayer(true);
                    // Set text in top text view
                    // Check if player has won
                }
                gameBtns[index].setEnabled(false);
            }
        }
    }
}
