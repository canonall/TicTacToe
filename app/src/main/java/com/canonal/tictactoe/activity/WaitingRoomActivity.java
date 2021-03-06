package com.canonal.tictactoe.activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.FragmentManager;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.canonal.tictactoe.R;
import com.canonal.tictactoe.adapter.WaitingRoomAdapter;
import com.canonal.tictactoe.dialog.GameInviteDialog;
import com.canonal.tictactoe.dialog.UsernameDialog;
import com.canonal.tictactoe.enums.InviteStatus;
import com.canonal.tictactoe.listener.GameInviteDialogListener;
import com.canonal.tictactoe.listener.RvWaitingRoomItemClickListener;
import com.canonal.tictactoe.listener.UsernameDialogListener;
import com.canonal.tictactoe.model.ActiveGame;
import com.canonal.tictactoe.model.GameInvite;
import com.canonal.tictactoe.model.OPlayer;
import com.canonal.tictactoe.model.Player;
import com.canonal.tictactoe.model.XPlayer;
import com.canonal.tictactoe.utility.operator.ActiveGameOperator;
import com.canonal.tictactoe.utility.operator.FirebaseOperator;
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

    private DatabaseReference gameInviteReference;
    private ValueEventListener gameInviteListener;

    private boolean addedToWaitingRoom = false;
    private boolean isGameInviteDialogOpen = false;
    private boolean isUsernameDialogOpen = false;

    private GameInvite mGameInvite;
    private GameInviteDialog gameInviteDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiting_room);
        ButterKnife.bind(this);

        Log.d(TAG, "onCreate: ");
        createUsernameDialog();

        gameInviteReference = FirebaseDatabase.getInstance().getReference().child(getString(R.string.path_gameInvite));
        gameInviteListener = new ValueEventListener() {
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
                                    " " + gameInvite.getInvitee().getPlayer().getUsername() + " " + getString(R.string.reject_request), Toast.LENGTH_LONG)
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

        gameInviteReference.addValueEventListener(gameInviteListener);

    }

    private void addToWaitingRoom(Player myPlayer) {

        //add player to waitingRoom database
        FirebaseOperator.pushPlayerToWaitingRoom(myPlayer, this);

        addedToWaitingRoom = true;
        listAllAvailablePlayers(myPlayer);

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
        FirebaseOperator.removePlayerFromWaitingRoom(myPlayer, this);
    }

    @Override
    public void createNewPlayer(String userId, String username) {
        isUsernameDialogOpen = false;

        myPlayer = new Player();
        myPlayer.setUserId(userId);
        myPlayer.setUsername(username);

        signInAnonymously(myPlayer);

        Log.d(TAG, "getUsername: " + username);
        Log.d(TAG, "getUserId: " + userId);

    }

    @Override
    public void cancelUsernameDialog() {
        isUsernameDialogOpen = false;
        finish();
    }

    private void createUsernameDialog() {

        isUsernameDialogOpen = true;

        UsernameDialog usernameDialog = new UsernameDialog(this);
        usernameDialog.show(getSupportFragmentManager(), "Username Dialog");
        usernameDialog.setCancelable(false);

    }

    private void createGameInviteDialog(GameInvite gameInvite) {

        mGameInvite = gameInvite;
        isGameInviteDialogOpen = true;

        FragmentManager fragmentManager = getSupportFragmentManager();

        gameInviteDialog = new GameInviteDialog(this, gameInvite, false);
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

        isGameInviteDialogOpen = false;

        XPlayer xPlayer = ActiveGameOperator.getXPlayer(gameInvite.getInviter().getPlayer(), this);
        OPlayer oPlayer = ActiveGameOperator.getOPlayer(gameInvite.getInvitee().getPlayer(), this);
        ActiveGame activeGame = ActiveGameOperator.getActiveGame(xPlayer, oPlayer);

        FirebaseOperator.pushActiveGame(activeGame, this);
        FirebaseOperator.removeGameInvite(gameInvite, this);
        createMatch(activeGame);
    }

    @Override
    public void rejectGameInvite(GameInvite gameInvite) {

        isGameInviteDialogOpen = false;

        FirebaseOperator.removeGameInvite(gameInvite, this);
        Log.d(TAG, "rejectGameInvite: invite rejected by invitee" + gameInvite.getInvitee().getPlayer().getUsername());
    }

    private void createMatch(ActiveGame activeGame) {

        //prevents listener to listen from another activity
        gameInviteReference.removeEventListener(gameInviteListener);

        Intent intent = new Intent(WaitingRoomActivity.this, OnlineGameActivity.class);
        intent.putExtra(getString(R.string.intent_my_player), myPlayer);
        intent.putExtra(getString(R.string.intent_active_game), activeGame);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause: ");

        if (isGameInviteDialogOpen) {

            FirebaseDatabase.getInstance().getReference().child(getResources().getString(R.string.path_gameInvite))
                    .child(mGameInvite.getInvitee().getPlayer().getUserId())
                    .child(getResources().getString(R.string.path_inviteStatus))
                    .setValue(InviteStatus.REJECTED);

            FirebaseOperator.removeGameInvite(mGameInvite, this);

            gameInviteDialog.dismiss();
            isGameInviteDialogOpen = false;
        }

        if (addedToWaitingRoom) {
            WaitingRoomOperator.removePlayerFromWaitingRoom(myPlayer, this);
            addedToWaitingRoom = false;
        }

    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop: WaitingRoom");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: wWaitingRoom");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: WaitingRoom");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG, "onRestart: WaitingRoom");

        if (!isUsernameDialogOpen) {
            addToWaitingRoom(myPlayer);
            addedToWaitingRoom = true;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: WaitingRoom");
    }
}
