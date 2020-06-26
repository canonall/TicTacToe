package com.canonal.tictactoe.activity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.canonal.tictactoe.R;
import com.canonal.tictactoe.adapter.WaitingRoomAdapter;
import com.canonal.tictactoe.dialog.UsernameDialog;
import com.canonal.tictactoe.listener.UsernameDialogListener;
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

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WaitingRoomActivity extends AppCompatActivity implements WaitingRoomAdapter.OnItemClickListener, UsernameDialogListener {

    private static final String TAG = "OnlineGame";

    @BindView(R.id.rv_waiting_room_players)
    RecyclerView rvWaitingRoomPlayers;

    //private Player player;
    //private int playerCount = 0;


    private DatabaseReference databaseReference;

    private List<Player> playerList;
    private String userId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiting_room);
        ButterKnife.bind(this);

        playerList = new ArrayList<>();


        getUserInfoFromDialog();

    }

    private void addToWaitingRoom(final Player player) {
        //get reference and add player to the database
        databaseReference = FirebaseDatabase.getInstance().getReference("waitingRoom");
        databaseReference.child(player.getUserId()).setValue(player);

        listAllAvailablePlayers(databaseReference);

    }

    private void listAllAvailablePlayers(DatabaseReference databaseReference) {

        //called once instead constantly
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //   playerCount = (int) dataSnapshot.getChildrenCount();
                //   playerCount = playerCount + 1;
                //   databaseReference.child(String.valueOf(playerCount)).setValue(player);

                //TODO First player's playerList did NOT update itself
                //TODO remove and add players are not in real time
                //TODO Add a listener for changes
                for (DataSnapshot child : dataSnapshot.getChildren()) {

                    Player player = new Player();
                    player.setUserId(child.child("userId").getValue().toString());
                    player.setUsername(child.child("username").getValue().toString());
                    playerList.add(player);

                }

                initiateRecyclerView(playerList);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //TODO
            }
        });
    }

    private void signInAnonymously(final String userId, final String username) {

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        firebaseAuth.signInAnonymously().addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {

                    //If sign-in success set userId
                    Player player = new Player();
                    player.setUserId(userId);
                    player.setUsername(username);

                    Log.d(TAG, "onComplete: userID: " + userId);
                    Log.d(TAG, "getUsername: " + username);


                    addToWaitingRoom(player);

                } else {
                    //Sign-in fails
                    Log.w(TAG, "signInAnonymously:failure", task.getException());
                    Toast.makeText(WaitingRoomActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void initiateRecyclerView(List<Player> playerList) {

        rvWaitingRoomPlayers.setLayoutManager(new LinearLayoutManager(this));
        WaitingRoomAdapter waitingRoomAdapter = new WaitingRoomAdapter(playerList, this, this);
        rvWaitingRoomPlayers.setAdapter(waitingRoomAdapter);

    }

    @Override
    public void OnItemClick(int position) {
        //TODO
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        databaseReference = FirebaseDatabase.getInstance().getReference("waitingRoom").child(userId);
        databaseReference.removeValue();
        //TODO Remove player from waiting list
    }

    @Override
    public void getUserInfo(String userId, String username) {
        this.userId = userId;

        signInAnonymously(userId, username);

        Log.d(TAG, "getUsername: " + username);
        Log.d(TAG, "getUserId: " + userId);

    }

    private void getUserInfoFromDialog() {

        UsernameDialog usernameDialog = new UsernameDialog();
        usernameDialog.show(getSupportFragmentManager(), "Username Dialog");
        usernameDialog.setCancelable(false);

    }

}
