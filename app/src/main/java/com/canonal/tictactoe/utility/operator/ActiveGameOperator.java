package com.canonal.tictactoe.utility.operator;

import android.content.Context;

import com.canonal.tictactoe.R;
import com.canonal.tictactoe.model.ActiveGame;
import com.canonal.tictactoe.model.OPlayer;
import com.canonal.tictactoe.model.Player;
import com.canonal.tictactoe.model.XPlayer;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;


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

    public static boolean checkCurrentTurnPlayer(Player player, ActiveGame activeGame) {
        return player.getUserId().equals(activeGame.getCurrentTurnPlayer().getUserId())
                && player.getUsername().equals(activeGame.getCurrentTurnPlayer().getUsername());
    }

    public static boolean checkXPlayer(Player player, ActiveGame activeGame) {
        return player.getUserId().equals(activeGame.getxPlayer().getPlayer().getUserId()) &&
                player.getUsername().equals(activeGame.getxPlayer().getPlayer().getUsername());
    }

    public static ActiveGame changeSides(ActiveGame activeGame, Context context) {

        XPlayer updatedXPlayer = getXPlayer(activeGame.getoPlayer().getPlayer(), context);
        OPlayer updatedOPlayer = getOPlayer(activeGame.getxPlayer().getPlayer(), context);

        return getActiveGame(updatedXPlayer, updatedOPlayer);

    }
}
