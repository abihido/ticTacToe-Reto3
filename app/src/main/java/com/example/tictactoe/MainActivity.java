package com.example.tictactoe;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {
    // Buttons making up the board
    private Button boardButtons[];
    private Toast toast;
    private int selected;
    // Various text displayed
    private TextView mInfoTextView,pcScore,human,empate;
    private TicTacToeGame mGame;
    private boolean GameOver,humanStarts;
    private Integer humanSc=0,androidScore=0,empateScore=0;
    static final int DIALOG_DIFFICULTY_ID = 0;
    static final int DIALOG_QUIT_ID = 1;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.new_game:
                startNewGame();
                return true;
            case R.id.ai_difficulty:
                showDialog(DIALOG_DIFFICULTY_ID);
                return true;
            case R.id.quit:
                showDialog(DIALOG_QUIT_ID);
                return true;
        }
        return false;
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        Dialog dialog = null;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        switch (id) {
            case DIALOG_DIFFICULTY_ID:
                builder.setTitle(R.string.difficulty_choose);
                final CharSequence[] levels = {
                        getResources().getString(R.string.difficulty_easy),
                        getResources().getString(R.string.difficulty_harder),
                        getResources().getString(R.string.difficulty_expert)};
// TODO: Set selected, an integer (0 to n-1), for the Difficulty dialog.
// selected is the radio button that should be selected.
                builder.setSingleChoiceItems(levels, selected,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int item) {
                                dialog.dismiss(); // Close dialog
// TODO: Set the diff level of mGame based on which item was selected.
// Display the selected difficulty level
                                switch (item){
                                    case 0:

                                        mGame.setDifficultyLevel(TicTacToeGame.DifficultyLevel.Easy);
                                        break;

                                    case 1:
                                        mGame.setDifficultyLevel(TicTacToeGame.DifficultyLevel.Harder);
                                    break;

                                    case 2:
                                        mGame.setDifficultyLevel(TicTacToeGame.DifficultyLevel.Expert);
                                    break;
                                }

                                Toast.makeText(getApplicationContext(), levels[item], Toast.LENGTH_SHORT).show();

                            }
                        });
                dialog = builder.create();
                break;

            case DIALOG_QUIT_ID:
// Create the quit confirmation dialog
                builder.setMessage(R.string.quit_question)
                        .setCancelable(false)
                        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                MainActivity.this.finish();
                            }
                        })
                        .setNegativeButton(R.string.no, null);
                dialog = builder.create();
                break;
        }
        return dialog;
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
        toast = Toast.makeText(this,"",Toast.LENGTH_SHORT);
        selected = 0;
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




