package com.canonal.tictactoe.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.canonal.tictactoe.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class GameActivity extends AppCompatActivity {

    @BindView(R.id.btn_00)
    Button btn00;
    @BindView(R.id.btn_01)
    Button btn01;
    @BindView(R.id.btn_02)
    Button btn02;
    @BindView(R.id.btn_10)
    Button btn10;
    @BindView(R.id.btn_11)
    Button btn11;
    @BindView(R.id.btn_12)
    Button btn12;
    @BindView(R.id.btn_20)
    Button btn20;
    @BindView(R.id.btn_21)
    Button btn21;
    @BindView(R.id.btn_22)
    Button btn22;
    @BindView(R.id.tv_winner)
    TextView tvWinner;
    @BindView(R.id.tv_play_again)
    TextView tvPlayAgain;

    private int roundCount = 0;
    private boolean player1Turn = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btn_00)
    public void onBtn00Clicked() {
        click(btn00);
    }

    @OnClick(R.id.btn_01)
    public void onBtn01Clicked() {
        click(btn01);
    }

    @OnClick(R.id.btn_02)
    public void onBtn02Clicked() {
        click(btn02);
    }

    @OnClick(R.id.btn_10)
    public void onBtn10Clicked() {
        click(btn10);
    }

    @OnClick(R.id.btn_11)
    public void onBtn11Clicked() {
        click(btn11);
    }

    @OnClick(R.id.btn_12)
    public void onBtn12Clicked() {
        click(btn12);
    }

    @OnClick(R.id.btn_20)
    public void onBtn20Clicked() {
        click(btn20);
    }

    @OnClick(R.id.btn_21)
    public void onBtn21Clicked() {
        click(btn21);
    }

    @OnClick(R.id.btn_22)
    public void onBtn22Clicked() {
        click(btn22);
    }

    private void click(Button btn) {

        if (player1Turn) {

            btn.setTextColor(getResources().getColor(R.color.colorAccent));
            btn.setText(getResources().getString(R.string.x));

        } else {

            btn.setTextColor(getResources().getColor(R.color.colorPrimary));
            btn.setText(getResources().getString(R.string.o));

        }

        roundCount++;

        //Check winner after  round 5
        //Before that nobody can win
        if (roundCount >= 5) {

            //check if there is a winner
            if (checkForWin()) {
                announceWinner(player1Turn);
            }
            //if roundCount is 9, then draw
            //else continue;
            else if (roundCount == 9) {
                callTieGame();
            }

        }

        //change player turn
        player1Turn = !player1Turn;

        //user cant click the same button
        btn.setEnabled(false);
    }


    private boolean checkForWin() {

        String[][] buttonStatus = new String[3][3];

        buttonStatus[0][0] = btn00.getText().toString();
        buttonStatus[0][1] = btn01.getText().toString();
        buttonStatus[0][2] = btn02.getText().toString();
        buttonStatus[1][0] = btn10.getText().toString();
        buttonStatus[1][1] = btn11.getText().toString();
        buttonStatus[1][2] = btn12.getText().toString();
        buttonStatus[2][0] = btn20.getText().toString();
        buttonStatus[2][1] = btn21.getText().toString();
        buttonStatus[2][2] = btn22.getText().toString();

        //there are eight different positions to end the game
        //3 rows
        //3 column
        //2 diagonal

        //check all rows
        for (int i = 0; i < 3; i++) {
            if (buttonStatus[i][0].equals(buttonStatus[i][1])
                    && buttonStatus[i][0].equals(buttonStatus[i][2])
                    && !buttonStatus[i][0].equals("")) {
                return true;
            }
        }

        //check all columns
        for (int i = 0; i < 3; i++) {
            if (buttonStatus[0][i].equals(buttonStatus[1][i])
                    && buttonStatus[0][i].equals(buttonStatus[2][i])
                    && !buttonStatus[0][i].equals("")) {
                return true;
            }
        }

        //check first diagonal
        if (buttonStatus[0][0].equals(buttonStatus[1][1])
                && buttonStatus[0][0].equals(buttonStatus[2][2])
                && !buttonStatus[0][0].equals("")) {
            return true;
        }

        //check second diagonal
        if (buttonStatus[0][2].equals(buttonStatus[1][1])
                && buttonStatus[0][2].equals(buttonStatus[2][0])
                && !buttonStatus[0][2].equals("")) {
            return true;
        }

        //no winners
        return false;
    }

    private void announceWinner(boolean player1Turn) {

        if (player1Turn) {
            //player1 wins
            tvWinner.setText(getResources().getString(R.string.player1_wins));
            tvWinner.setTextColor(getResources().getColor(R.color.colorAccent));

        } else {
            //player2 wins
            tvWinner.setText(getResources().getString(R.string.player2_wins));
            tvWinner.setTextColor(getResources().getColor(R.color.colorPrimary));

        }

        disableButtons();
        makeRestartVisible();
        makeWinnerVisible();

    }

    private void callTieGame() {

        tvWinner.setText(getResources().getString(R.string.draw));
        tvWinner.setTextColor(getResources().getColor(R.color.tieGame));

        disableButtons();
        makeRestartVisible();
        makeWinnerVisible();

    }


    @OnClick(R.id.tv_play_again)
    public void onTvPlayAgainClicked() {

        enableButtons();
        resetButtonStatus();
        makeRestartInvisible();
        makeWinnerInvisible();

        roundCount = 0;
        player1Turn = true;
    }

    private void makeRestartVisible() {
        tvPlayAgain.setVisibility(View.VISIBLE);
    }

    private void makeRestartInvisible() {
        tvPlayAgain.setVisibility(View.GONE);

    }

    private void makeWinnerInvisible() {
        tvWinner.setVisibility(View.GONE);
    }

    private void makeWinnerVisible() {
        tvWinner.setVisibility(View.VISIBLE);
    }

    private void disableButtons() {

        btn00.setEnabled(false);
        btn01.setEnabled(false);
        btn02.setEnabled(false);
        btn10.setEnabled(false);
        btn11.setEnabled(false);
        btn12.setEnabled(false);
        btn20.setEnabled(false);
        btn21.setEnabled(false);
        btn22.setEnabled(false);

    }

    private void enableButtons() {

        btn00.setEnabled(true);
        btn01.setEnabled(true);
        btn02.setEnabled(true);
        btn10.setEnabled(true);
        btn11.setEnabled(true);
        btn12.setEnabled(true);
        btn20.setEnabled(true);
        btn21.setEnabled(true);
        btn22.setEnabled(true);

    }

    private void resetButtonStatus() {

        btn00.setText("");
        btn01.setText("");
        btn02.setText("");
        btn10.setText("");
        btn11.setText("");
        btn12.setText("");
        btn20.setText("");
        btn21.setText("");
        btn22.setText("");

    }
}
