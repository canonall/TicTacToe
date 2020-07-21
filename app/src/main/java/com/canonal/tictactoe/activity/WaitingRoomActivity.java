package com.canonal.tictactoe.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.canonal.tictactoe.R;
import com.canonal.tictactoe.adapter.WaitingRoomAdapter;
import com.canonal.tictactoe.dialog.GameInviteDialog;
import com.canonal.tictactoe.dialog.UsernameDialog;
import com.canonal.tictactoe.listener.GameInviteDialogListener;
import com.canonal.tictactoe.listener.RvWaitingRoomItemClickListener;
import com.canonal.tictactoe.listener.UsernameDialogListener;
import com.canonal.tictactoe.model.ActiveGame;
import com.canonal.tictactoe.model.GameInvite;
import com.canonal.tictactoe.model.OPlayer;
import com.canonal.tictactoe.model.Player;
import com.canonal.tictactoe.model.XPlayer;
import com.canonal.tictactoe.utility.operator.ActiveGameOperator;
import com.canonal.tictactoe.utility.operator.GameInviteOperator;
import com.canonal.tictactoe.utility.operator.WaitingRoomOperator;
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

public class WaitingRoomActivity extends AppCompatActivity implements RvWaitingRoomItemClickListener, UsernameDialogListener, GameInviteDialogListener {

    private static final String TAG = "WAITING_ROOM";

    @BindView(R.id.rv_waiting_room_players)
    RecyclerView rvWaitingRoomPlayers;

    private Player myPlayer;
    private DatabaseReference databaseReference;
    private ValueEventListener valueEventListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiting_room);
        ButterKnife.bind(this);

        getUserInfoFromDialog();

    }

    private void addToWaitingRoom(final Player myPlayer) {
        //get reference and add player to the database
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child(getResources().getString(R.string.path_waitingRoom));
        databaseReference.child(myPlayer.getUserId()).child(getResources().getString(R.string.path_player)).setValue(myPlayer);

        listAllAvailablePlayers(myPlayer);
        checkGameInvites();

    }

    private void listAllAvailablePlayers(final Player myPlayer) {

        FirebaseDatabase.getInstance().getReference().child(getResources().getString(R.string.path_waitingRoom))
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        Set<Player> playerSet = new TreeSet<>();

                        for (DataSnapshot child : dataSnapshot.getChildren()) {

                            //get each player from firebase and add them to set
                            Player player = child.child(getResources().getString(R.string.path_player)).getValue(Player.class);
                            playerSet.add(player);

                        }

                        initiateRecyclerView(playerSet, myPlayer);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        //TODO
                    }
                });
    }

    private void checkGameInvites() {
        databaseReference = FirebaseDatabase.getInstance().getReference().child(getString(R.string.path_gameInvite));
        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren()) {

                    GameInvite gameInvite = child.getValue(GameInvite.class);

                    if (gameInvite != null) {

                        Log.d(TAG, "onDataChange: received invitee ID-->" + gameInvite.getInvitee().getPlayer().getUserId());
                        Log.d(TAG, "onDataChange: received invitee  Name-->" + gameInvite.getInvitee().getPlayer().getUsername());

                        if (GameInviteOperator.isMyPlayerInvited(gameInvite, myPlayer)) {
                            createGameInviteDialog(gameInvite);
                            break;
                        }

                        if (GameInviteOperator.isMyInviteAccepted(gameInvite, myPlayer)) {

                            FirebaseDatabase.getInstance().getReference().child(getString(R.string.path_activeGame))
                                    .child(gameInvite.getInvitee().getPlayer().getUserId() + gameInvite.getInviter().getPlayer().getUserId())
                                    .addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            ActiveGame activeGame = dataSnapshot.getValue(ActiveGame.class);
                                            createMatch(activeGame);
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });

                        } else if (GameInviteOperator.isMyInviteRejected(gameInvite, myPlayer)) {
                            Toast.makeText(getApplicationContext(),
                                    " " + gameInvite.getInvitee().getPlayer().getUsername() + " has rejected your request", Toast.LENGTH_LONG)
                                    .show();
                        }
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        databaseReference.addValueEventListener(valueEventListener);

    }

    private void signInAnonymously(final Player myPlayer) {

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        firebaseAuth.signInAnonymously().addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {

                    //If sign-in success add player to waiting room
                    addToWaitingRoom(myPlayer);

                    Log.d(TAG, "onComplete: userID: " + myPlayer.getUserId());
                    Log.d(TAG, "getUsername: " + myPlayer.getUsername());

                } else {
                    //Sign-in fails
                    Log.w(TAG, "signInAnonymously:failure", task.getException());
                    Toast.makeText(WaitingRoomActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void initiateRecyclerView(Set<Player> playerSet, Player myPlayer) {

        rvWaitingRoomPlayers.setLayoutManager(new LinearLayoutManager(this));
        WaitingRoomAdapter waitingRoomAdapter = new WaitingRoomAdapter(playerSet, myPlayer, this, this);
        rvWaitingRoomPlayers.setAdapter(waitingRoomAdapter);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        FirebaseDatabase.getInstance().getReference()
                .child(getResources().getString(R.string.path_waitingRoom))
                .child(myPlayer.getUserId())
                .removeValue();
    }

    @Override
    public void createNewPlayer(String userId, String username) {

        myPlayer = new Player();
        myPlayer.setUserId(userId);
        myPlayer.setUsername(username);

        signInAnonymously(myPlayer);

        Log.d(TAG, "getUsername: " + username);
        Log.d(TAG, "getUserId: " + userId);

    }

    private void getUserInfoFromDialog() {

        UsernameDialog usernameDialog = new UsernameDialog(this);
        usernameDialog.show(getSupportFragmentManager(), "Username Dialog");
        usernameDialog.setCancelable(false);

    }

    private void createGameInviteDialog(GameInvite gameInvite) {

        FragmentManager fragmentManager = getSupportFragmentManager();

        GameInviteDialog gameInviteDialog = new GameInviteDialog(this, gameInvite, false);
        fragmentManager.beginTransaction().add(gameInviteDialog, "GameInvite Dialog").commitAllowingStateLoss();
        gameInviteDialog.setCancelable(false);

    }

    @Override
    public void onRvItemClick(int position) {
        //if user clicks invite, onItemClick not working
        //if user clicks onItemClick, invite not working
        Log.d(TAG, "OnItemClick: RV CLICKED");
    }

    @Override
    public void acceptGameInvite(GameInvite gameInvite) {

        XPlayer xPlayer = ActiveGameOperator.getXPlayer(gameInvite.getInviter().getPlayer(), this);
        OPlayer oPlayer = ActiveGameOperator.getOPlayer(gameInvite.getInvitee().getPlayer(), this);
        ActiveGame activeGame = ActiveGameOperator.getActiveGame(xPlayer, oPlayer);

        ActiveGameOperator.pushActiveGameToFirebase(activeGame, this);
        GameInviteOperator.removePlayersFromGameInvite(gameInvite, this);
        WaitingRoomOperator.removePlayersFromWaitingRoom(activeGame, this);

        createMatch(activeGame);
    }

    @Override
    public void rejectGameInvite(GameInvite gameInvite) {

        GameInviteOperator.removePlayersFromGameInvite(gameInvite, this);
        Log.d(TAG, "rejectGameInvite: invite rejected by invitee" + gameInvite.getInvitee().getPlayer().getUsername());
    }


    private void createMatch(ActiveGame activeGame) {

        //prevents listener to listen from another activity
        databaseReference.removeEventListener(valueEventListener);

        Intent intent = new Intent(WaitingRoomActivity.this, OnlineGameActivity.class);
        intent.putExtra(getString(R.string.intent_my_player), myPlayer);
        intent.putExtra(getString(R.string.intent_active_game), activeGame);
        startActivity(intent);
    }

}
