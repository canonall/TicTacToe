package com.canonal.tictactoe.utility.operator;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.canonal.tictactoe.adapter.WaitingRoomAdapter;
import com.canonal.tictactoe.model.Player;

import java.util.List;
import java.util.Set;

public class GameUiOperator {
    public static void disableButtons(List<Button> buttons) {
        for (Button button : buttons) {
            button.setEnabled(false);
        }
    }

    public static void enableButtons(List<Button> buttons) {
        for (Button button : buttons) {
            button.setEnabled(true);
        }
    }

    public static void resetButtonStatus(List<Button> buttons) {
        for (Button button : buttons) {
            button.setText("");
        }
    }

    public static void makeRestartVisible(TextView tvPlayAgain) {
        tvPlayAgain.setVisibility(View.VISIBLE);
    }

    public static void makeRestartInvisible(TextView tvPlayAgain) {
        tvPlayAgain.setVisibility(View.GONE);
    }

    public static void makeWinnerInvisible(TextView tvWinner) {
        tvWinner.setVisibility(View.GONE);
    }

    public static void makeWinnerVisible(TextView tvWinner) {
        tvWinner.setVisibility(View.VISIBLE);
    }

    public static void showGameFinishedUi(List<Button> buttonList, TextView tvPlayAgain, TextView tvWinner) {
        disableButtons(buttonList);
        makeRestartVisible(tvPlayAgain);
        makeWinnerVisible(tvWinner);
    }

}
