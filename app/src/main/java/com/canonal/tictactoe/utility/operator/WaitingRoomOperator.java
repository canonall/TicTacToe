package com.canonal.tictactoe.utility.operator;

import android.content.Context;

import com.canonal.tictactoe.R;
import com.canonal.tictactoe.model.ActiveGame;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class WaitingRoomOperator {
    public static void removeFromWaitingRoom(ActiveGame activeGame, Context context) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference()
                .child(context.getResources().getString(R.string.path_waitingRoom));

        databaseReference.child(activeGame.getXplayer().getPlayer().getUserId()).removeValue();
        databaseReference.child(activeGame.getOplayer().getPlayer().getUserId()).removeValue();

    }
}
