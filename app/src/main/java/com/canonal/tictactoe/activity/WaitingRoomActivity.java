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

import java.util.Set;
import java.util.TreeSet;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WaitingRoomActivity extends AppCompatActivity implements WaitingRoomAdapter.OnItemClickListener, UsernameDialogListener {

    private static final String TAG = "OnlineGame";

    @BindView(R.id.rv_waiting_room_players)
    RecyclerView rvWaitingRoomPlayers;

    private DatabaseReference databaseReference;
    private Player player;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiting_room);
        ButterKnife.bind(this);

        getUserInfoFromDialog();

    }

    private void addToWaitingRoom(final Player player) {
        //get reference and add player to the database
        databaseReference = FirebaseDatabase.getInstance().getReference("waitingRoom");
        databaseReference.child(player.getUserId()).setValue(player);

        listAllAvailablePlayers(databaseReference);

    }

    private void listAllAvailablePlayers(DatabaseReference databaseReference) {

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Set<Player> playerSet = new TreeSet<>();

                for (DataSnapshot child : dataSnapshot.getChildren()) {

                    //create all players from stretch
                    Player player = new Player();
                    player.setUserId(child.child("userId").getValue().toString());
                    player.setUsername(child.child("username").getValue().toString());
                    playerSet.add(player);

                }

                initiateRecyclerView(playerSet);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //TODO
            }
        });


    }

    private void signInAnonymously(final Player player) {

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        firebaseAuth.signInAnonymously().addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {

                    //If sign-in success add player to waiting room
                    addToWaitingRoom(player);

                    Log.d(TAG, "onComplete: userID: " + player.getUserId());
                    Log.d(TAG, "getUsername: " + player.getUsername());

                } else {
                    //Sign-in fails
                    Log.w(TAG, "signInAnonymously:failure", task.getException());
                    Toast.makeText(WaitingRoomActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void initiateRecyclerView(Set<Player> playerSet) {

        rvWaitingRoomPlayers.setLayoutManager(new LinearLayoutManager(this));
        WaitingRoomAdapter waitingRoomAdapter = new WaitingRoomAdapter(playerSet, this, this);
        rvWaitingRoomPlayers.setAdapter(waitingRoomAdapter);

    }

    @Override
    public void onItemClick(int position) {
        //TODO
        //if user clicks invite, onItemClick not working
        //if user clicks onItemClick, invite not working
        Log.d(TAG, "OnItemClick: RV CLICKED");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        databaseReference = FirebaseDatabase.getInstance().getReference("waitingRoom").child(player.getUserId());
        databaseReference.removeValue();
    }

    @Override
    public void createNewPlayer(String userId, String username) {
        player = new Player();
        player.setUserId(userId);
        player.setUsername(username);

        signInAnonymously(player);

        Log.d(TAG, "getUsername: " + username);
        Log.d(TAG, "getUserId: " + userId);

    }

    private void getUserInfoFromDialog() {

        UsernameDialog usernameDialog = new UsernameDialog(this);
        usernameDialog.show(getSupportFragmentManager(), "Username Dialog");
        usernameDialog.setCancelable(false);

    }

}
