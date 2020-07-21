package com.canonal.tictactoe.utility.operator;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.canonal.tictactoe.R;
import com.canonal.tictactoe.activity.OnlineGameActivity;
import com.canonal.tictactoe.activity.WaitingRoomActivity;
import com.canonal.tictactoe.model.ActiveGame;
import com.canonal.tictactoe.model.OPlayer;
import com.canonal.tictactoe.model.Player;
import com.canonal.tictactoe.model.XPlayer;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class ActiveGameOperator {

    public static ActiveGame getActiveGame(XPlayer xplayer, OPlayer oplayer) {
        ActiveGame activeGame = new ActiveGame();
        activeGame.setxPlayer(xplayer);
        activeGame.setoPlayer(oplayer);
        activeGame.setRoundCount(0);
        activeGame.setCurrentTurnPlayer(xplayer.getPlayer());
        return activeGame;
    }

    public static OPlayer getOPlayer(Player inviteePlayer, Context context) {
        OPlayer oPlayer = new OPlayer();
        oPlayer.setPlayer(inviteePlayer);
        oPlayer.setSymbol(context.getResources().getString(R.string.o));
        return oPlayer;

    }

    public static XPlayer getXPlayer(Player inviterPlayer, Context context) {
        XPlayer xPlayer = new XPlayer();
        xPlayer.setPlayer(inviterPlayer);
        xPlayer.setSymbol(context.getResources().getString(R.string.x));
        return xPlayer;
    }

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

    public static boolean checkCurrentTurnPlayer(Player player, ActiveGame activeGame) {
        return player.getUserId().equals(activeGame.getCurrentTurnPlayer().getUserId())
                && player.getUsername().equals(activeGame.getCurrentTurnPlayer().getUsername());
    }

    public static boolean checkXPlayer(Player player, ActiveGame activeGame) {
        return player.getUserId().equals(activeGame.getxPlayer().getPlayer().getUserId()) &&
                player.getUsername().equals(activeGame.getxPlayer().getPlayer().getUsername());
    }

    public static void showGameFinishedUi(List<Button> buttonList, TextView tvPlayAgain, TextView tvWinner) {
        disableButtons(buttonList);
        makeRestartVisible(tvPlayAgain);
        makeWinnerVisible(tvWinner);
    }

    public static void pushActiveGameToFirebase(ActiveGame activeGame, Context context) {

        FirebaseDatabase.getInstance().getReference()
                .child(context.getString(R.string.path_activeGame))
                .child(activeGame.getoPlayer().getPlayer().getUserId() + activeGame.getxPlayer().getPlayer().getUserId())
                .setValue(activeGame);
    }

    public static ActiveGame changeSides(ActiveGame activeGame, Context context) {

        XPlayer updatedXPlayer = getXPlayer(activeGame.getoPlayer().getPlayer(), context);
        OPlayer updatedOPlayer = getOPlayer(activeGame.getxPlayer().getPlayer(), context);
        return getActiveGame(updatedXPlayer, updatedOPlayer);

    }

    public static void removeActiveGame(ActiveGame activeGame, Context context) {

        FirebaseDatabase.getInstance().getReference().child(context.getString(R.string.path_activeGame))
                .child(activeGame.getoPlayer().getPlayer().getUserId() + activeGame.getxPlayer().getPlayer().getUserId())
                .removeValue();
    }

}
