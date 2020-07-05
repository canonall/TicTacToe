package com.canonal.tictactoe.utility;

import android.content.Context;
import android.os.Parcelable;

import com.canonal.tictactoe.R;
import com.canonal.tictactoe.model.ActiveGame;
import com.canonal.tictactoe.model.Oplayer;
import com.canonal.tictactoe.model.Player;
import com.canonal.tictactoe.model.Xplayer;

public class ActiveGameOperator {
    public static ActiveGame getActiveGame(Xplayer xplayer, Oplayer oplayer) {
        ActiveGame activeGame = new ActiveGame();
        activeGame.setXplayer(xplayer);
        activeGame.setOplayer(oplayer);
        return activeGame;
    }

    public static Oplayer getOplayer(Player inviteePlayer, Context context) {
        Oplayer oplayer = new Oplayer();
        oplayer.setPlayer(inviteePlayer);
        oplayer.setSymbol(context.getResources().getString(R.string.o));
        return oplayer;

    }

    public static Xplayer getXplayer(Player inviterPlayer,Context context) {
        Xplayer xplayer = new Xplayer();
        xplayer.setPlayer(inviterPlayer);
        xplayer.setSymbol(context.getResources().getString(R.string.x));
        return xplayer;

    }
}
