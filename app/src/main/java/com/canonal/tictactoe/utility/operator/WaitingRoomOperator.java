package com.canonal.tictactoe.utility.operator;

import android.content.Context;

import com.canonal.tictactoe.R;
import com.canonal.tictactoe.model.ActiveGame;
import com.canonal.tictactoe.model.Player;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class WaitingRoomOperator {
    public static void removePlayersFromWaitingRoom(ActiveGame activeGame, Context context) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference()
                .child(context.getResources().getString(R.string.path_waitingRoom));

        databaseReference.child(activeGame.getxPlayer().getPlayer().getUserId()).removeValue();
        databaseReference.child(activeGame.getoPlayer().getPlayer().getUserId()).removeValue();

    }

    public static void removePlayerFromWaitingRoom(Player player, Context context) {
        FirebaseDatabase.getInstance().getReference()
                .child(context.getString(R.string.path_waitingRoom))
                .child(player.getUserId())
                .removeValue();

    }
}
