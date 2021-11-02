package com.example.tictactoe;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {
    // Buttons making up the board
    private Button boardButtons[];
    // Various text displayed
    private TextView mInfoTextView,pcScore,human,empate;
    private TicTacToeGame mGame;
    private boolean GameOver,humanStarts;
    private Integer humanSc=0,androidScore=0,empateScore=0;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        menu.add("New Game");
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        startNewGame();
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mGame= new TicTacToeGame();
        setContentView(R.layout.activity_main);
        boardButtons = new Button[TicTacToeGame.BOARD_SIZE];
        boardButtons[0] = (Button) findViewById(R.id.one);
        boardButtons[1] = (Button) findViewById(R.id.two);
        boardButtons[2] = (Button) findViewById(R.id.three);
        boardButtons[3] = (Button) findViewById(R.id.four);
        boardButtons[4] = (Button) findViewById(R.id.five);
        boardButtons[5] = (Button) findViewById(R.id.six);
        boardButtons[6] = (Button) findViewById(R.id.seven);
        boardButtons[7] = (Button) findViewById(R.id.eight);
        boardButtons[8] = (Button) findViewById(R.id.nine);
        mInfoTextView = (TextView) findViewById(R.id.information);
        human = (TextView) findViewById(R.id.humanScore);
        pcScore = (TextView) findViewById(R.id.pcScore);
        empate = (TextView) findViewById(R.id.empate);
        human.setText(humanSc.toString());
        pcScore.setText(androidScore.toString());
        empate.setText(empateScore.toString());
        humanStarts=true;

        startNewGame();
    }
    private void startNewGame() {
        mGame.clearBoard();
        for (int i = 0; i < boardButtons.length; i++) {
            boardButtons[i].setText("");
            boardButtons[i].setEnabled(true);
            boardButtons[i].setOnClickListener(new ButtonClickListener(i));
        }
        this.GameOver = false;
        if(humanStarts) {
            mInfoTextView.setText("you go first.");
            humanStarts=!humanStarts;
        }
        else {
            mInfoTextView.setText("android go first.");
            int move = mGame.getComputerMove();
            setMove(TicTacToeGame.COMPUTER_PLAYER, move);
            humanStarts=!humanStarts;
        }
    }
    private class ButtonClickListener implements View.OnClickListener {
        int location;
        public ButtonClickListener(int location) {
            this.location = location;
        }
        public void onClick(View view) {
            if (boardButtons[location].isEnabled()) {
                setMove(TicTacToeGame.HUMAN_PLAYER, location);
                // If no winner yet, let the computer make a move
                int winner = mGame.checkForWinner();
                if (winner == 0) {
                    mInfoTextView.setText("It's Android's turn.");
                    int move = mGame.getComputerMove();
                    setMove(TicTacToeGame.COMPUTER_PLAYER, move);
                    winner = mGame.checkForWinner();
                }
                if (winner == 0) {
                    mInfoTextView.setText("It's your turn.");
                }
                else if (winner == 1)
                {
                    mInfoTextView.setText("It's a tie!");
                    GameOver=true;
                    empateScore++;
                    empate.setText(empateScore.toString());
                }

                else if (winner == 2) {
                    mInfoTextView.setText("You won!");
                    GameOver = true;
                    humanSc++;
                    human.setText(humanSc.toString());
                }
                else {
                    mInfoTextView.setText("Android won!");
                    GameOver = true;
                    androidScore++;
                    pcScore.setText(androidScore.toString());
                }
            }
        }
    }

    private void setMove(char player, int location) {
        if (!GameOver) {
            mGame.setMove(player, location);
            boardButtons[location].setEnabled(false);
            boardButtons[location].setText(String.valueOf(player));
            if (player == TicTacToeGame.HUMAN_PLAYER)
                boardButtons[location].setTextColor(Color.rgb(0, 200, 0));
            else
                boardButtons[location].setTextColor(Color.rgb(200, 0, 0));
        }

    }
}




