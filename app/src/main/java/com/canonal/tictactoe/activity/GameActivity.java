package com.canonal.tictactoe.activity;

import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.canonal.tictactoe.R;
import com.canonal.tictactoe.utility.operator.GameUiOperator;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class GameActivity extends AppCompatActivity {

    private static final String TAG = "LOCAL GAME ACTIVITY";

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

    private List<Button> buttonList;

    private int roundCount = 0;
    private boolean player1Turn = true;

    private InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        ButterKnife.bind(this);

        initiateInterstitialAd();

        buttonList = addButtonsToList();

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

        GameUiOperator.showGameFinishedUi(buttonList, tvPlayAgain, tvWinner);

    }

    private void callTieGame() {

        tvWinner.setText(getResources().getString(R.string.draw));
        tvWinner.setTextColor(getResources().getColor(R.color.tieGame));

        GameUiOperator.showGameFinishedUi(buttonList, tvPlayAgain, tvWinner);

    }


    @OnClick(R.id.tv_play_again)
    public void onTvPlayAgainClicked() {
        resetGameBoard();
    }

    private void resetGameBoard() {
        GameUiOperator.enableButtons(buttonList);
        GameUiOperator.resetButtonStatus(buttonList);
        GameUiOperator.makeRestartInvisible(tvPlayAgain);
        GameUiOperator.makeWinnerInvisible(tvWinner);

        roundCount = 0;
        player1Turn = true;
    }

    private List<Button> addButtonsToList() {

        List<Button> buttonList = new ArrayList<>();
        buttonList.add(btn00);
        buttonList.add(btn01);
        buttonList.add(btn02);
        buttonList.add(btn10);
        buttonList.add(btn11);
        buttonList.add(btn12);
        buttonList.add(btn20);
        buttonList.add(btn21);
        buttonList.add(btn22);

        return buttonList;

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        showInterstitialAd();
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                startActivity(new Intent(GameActivity.this, MainActivity.class));
                finish();
            }

        });
    }

    private void initiateInterstitialAd() {
        mInterstitialAd = new InterstitialAd(this);
        //TODO when publish change unitId with original
        mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
    }

    private void showInterstitialAd() {
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        } else {
            Log.d(TAG, getString(R.string.ad_load_failed));
        }
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
}
