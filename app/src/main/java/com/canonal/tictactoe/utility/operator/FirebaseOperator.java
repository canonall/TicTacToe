package com.canonal.tictactoe.utility.operator;

import android.content.Context;
import com.canonal.tictactoe.R;
import com.canonal.tictactoe.model.ActiveGame;
import com.canonal.tictactoe.model.GameInvite;
import com.canonal.tictactoe.model.Player;
import com.google.firebase.database.FirebaseDatabase;

public class FirebaseOperator {

    public static void pushPlayerToWaitingRoom(Player player, Context context) {

        FirebaseDatabase.getInstance().getReference().child(context.getString(R.string.path_waitingRoom))
                .child(player.getUserId())
                .child(context.getString(R.string.path_player))
                .setValue(player);
    }

    public static void removePlayerFromWaitingRoom(Player player, Context context) {

        FirebaseDatabase.getInstance().getReference()
                .child(context.getString(R.string.path_waitingRoom))
                .child(player.getUserId())
                .removeValue();
    }

    public static void removeGameInvite(GameInvite gameInvite, Context context) {

        FirebaseDatabase.getInstance().getReference().child(context.getString(R.string.path_gameInvite))
                .child(gameInvite.getInvitee().getPlayer().getUserId())
                .removeValue();

    }

    public static void pushGameInvite(GameInvite gameInvite, Context context) {

        FirebaseDatabase.getInstance().getReference().child(context.getString(R.string.path_gameInvite))
                .child(gameInvite.getInvitee().getPlayer().getUserId())
                .setValue(gameInvite);

    }

    public static void pushActiveGame(ActiveGame activeGame, Context context) {

        FirebaseDatabase.getInstance().getReference().child(context.getString(R.string.path_activeGame))
                .child(activeGame.getoPlayer().getPlayer().getUserId() + activeGame.getxPlayer().getPlayer().getUserId())
                .setValue(activeGame);
    }

    public static void removeActiveGame(ActiveGame activeGame, Context context) {

        FirebaseDatabase.getInstance().getReference().child(context.getString(R.string.path_activeGame))
                .child(activeGame.getoPlayer().getPlayer().getUserId() + activeGame.getxPlayer().getPlayer().getUserId())
                .removeValue();
    }
}
