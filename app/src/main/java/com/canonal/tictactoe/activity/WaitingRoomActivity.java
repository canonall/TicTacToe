package com.canonal.tictactoe.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.canonal.tictactoe.R;
import com.canonal.tictactoe.adapter.WaitingRoomAdapter;
import com.canonal.tictactoe.model.Player;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WaitingRoomActivity extends AppCompatActivity implements WaitingRoomAdapter.OnItemClickListener {

    private static final String TAG = "OnlineGame";

    @BindView(R.id.rv_waiting_room_players)
    RecyclerView rvWaitingRoomPlayers;

    private Player player;
    private int playerCount = 0;

    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiting_room);
        ButterKnife.bind(this);

        firebaseAuth = FirebaseAuth.getInstance();
        signInAnonymously();
    }

    private void addToWaitingRoom(final Player player) {

        databaseReference = FirebaseDatabase.getInstance().getReference("waitingRoom");
        databaseReference.child(player.getUserId()).setValue(player);

        databaseReference = databaseReference.child(player.getUserId());

        //called once instead constantly
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //   playerCount = (int) dataSnapshot.getChildrenCount();
                //   playerCount = playerCount + 1;
                //   databaseReference.child(String.valueOf(playerCount)).setValue(player);

                initiateRecyclerView(dataSnapshot);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //TODO
            }
        });

    }

    private void signInAnonymously() {

        firebaseAuth.signInAnonymously().addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {
                    //If sign-in success set userId
                    player = new Player();
                    player.setUserId(firebaseAuth.getUid());
                    Log.d(TAG, "onComplete: UID: " + firebaseAuth.getUid());

                    addToWaitingRoom(player);

                } else {
                    //Sign-in fails
                    Log.w(TAG, "signInAnonymously:failure", task.getException());
                    Toast.makeText(WaitingRoomActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void initiateRecyclerView(DataSnapshot dataSnapshot) {

        rvWaitingRoomPlayers.setLayoutManager(new LinearLayoutManager(this));
        WaitingRoomAdapter waitingRoomAdapter = new WaitingRoomAdapter(dataSnapshot, this, this);
        rvWaitingRoomPlayers.setAdapter(waitingRoomAdapter);

    }

    @Override
    public void OnItemClick(int position) {
        //TODO
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //databaseReference = FirebaseDatabase.getInstance().getReference("waitingRoom").child();
        //TODO Remove player from waiting list
    }
}
