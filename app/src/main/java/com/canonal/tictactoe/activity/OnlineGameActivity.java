package com.canonal.tictactoe.activity;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.canonal.tictactoe.R;
import com.canonal.tictactoe.dialog.GameExitDialog;
import com.canonal.tictactoe.dialog.GameInviteDialog;
import com.canonal.tictactoe.enums.InviteStatus;
import com.canonal.tictactoe.listener.GameExitDialogListener;
import com.canonal.tictactoe.listener.GameInviteDialogListener;
import com.canonal.tictactoe.model.ActiveGame;
import com.canonal.tictactoe.model.GameInvite;
import com.canonal.tictactoe.model.Invitee;
import com.canonal.tictactoe.model.Inviter;
import com.canonal.tictactoe.model.Move;
import com.canonal.tictactoe.model.Player;
import com.canonal.tictactoe.utility.operator.ActiveGameOperator;
import com.canonal.tictactoe.utility.operator.FirebaseOperator;
import com.canonal.tictactoe.utility.operator.GameInviteOperator;
import com.canonal.tictactoe.utility.operator.GameUiOperator;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class OnlineGameActivity extends AppCompatActivity implements GameInviteDialogListener, GameExitDialogListener {

    private static final String TAG = "ONLINE_GAME_ACTIVITY";

    @BindView(R.id.btn_00)
    Button btn00;
    @BindView(R.id.btn_01)
    Button btn01;
    @BindView(R.id.btn_02)
    Button btn02;
    @BindView(R.id.btn_10)
    Button btn10;
    @BindView(R.id.btn_11)
    Button btn11;
    @BindView(R.id.btn_12)
    Button btn12;
    @BindView(R.id.btn_20)
    Button btn20;
    @BindView(R.id.btn_21)
    Button btn21;
    @BindView(R.id.btn_22)
    Button btn22;
    @BindView(R.id.tv_winner)
    TextView tvWinner;
    @BindView(R.id.tv_play_again)
    TextView tvPlayAgain;
    @BindView(R.id.tv_player1_name)
    TextView tvPlayer1Name;
    @BindView(R.id.tv_player2_name)
    TextView tvPlayer2Name;

    private List<Button> buttonList;
    private ActiveGame activeGame;
    private Player myPlayer;
    private GameInvite mGameInvite;
    private GameInviteDialog gameInviteDialog;

    private boolean amITheLeaver = true;
    private boolean isGameInviteOperation = false;
    private boolean isGameInviteDialogShowed = false;

    private DatabaseReference activeGameReference;
    private ValueEventListener activeGameEventListener;

    private DatabaseReference gameInviteReference;
    private ValueEventListener gameInviteEventListener;

    private InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online_game);
        ButterKnife.bind(this);

        initiateInterstitialAd();

        buttonList = addButtonsToList();
        setTagToButtons();

        getPlayersInfo();
        printPlayerNames();

        activeGameReference = FirebaseDatabase.getInstance().getReference()
                .child(getString(R.string.path_activeGame))
                .child(activeGame.getoPlayer().getPlayer().getUserId() + activeGame.getxPlayer().getPlayer().getUserId());

        activeGameEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                ActiveGame updatedActiveGame = dataSnapshot.getValue(ActiveGame.class);

                if (updatedActiveGame != null) {
                    if (updatedActiveGame.getMove() != null) {

                        String symbol = updatedActiveGame.getMove().getMoveSymbol();
                        String position = updatedActiveGame.getMove().getMovePosition();

                        for (Button button : buttonList) {
                            if (button.getTag().equals(position)) {
                                updateBoard(button, symbol);
                                checkWinner(updatedActiveGame);
                                break;
                            }
                        }

                    }

                    if (updatedActiveGame.isLeftDuringGame()) {

                        //TODO iki taraf için de çalışıyor
                        if (amITheLeaver) {
                            Toast.makeText(getApplication(), getString(R.string.exit_game_notify), Toast.LENGTH_LONG).show();
                            FirebaseOperator.removeActiveGame(updatedActiveGame, getApplicationContext());
                            amITheLeaver = false;
                        }


                        returnUserToWaitingRoom();

                    }

                }
                activeGame = updatedActiveGame;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        activeGameReference.addValueEventListener(activeGameEventListener);

        //read game invites
        gameInviteReference = FirebaseDatabase.getInstance().getReference().child(getResources().getString(R.string.path_gameInvite));
        gameInviteEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren()) {

                    GameInvite gameInvite = child.getValue(GameInvite.class);

                    if (gameInvite != null) {

                        if (GameInviteOperator.isMyPlayerInvited(gameInvite, myPlayer)) {
                            createGameInviteDialog(gameInvite);
                        }

                        if (GameInviteOperator.isMyInviteAccepted(gameInvite, myPlayer)) {
                            restartActiveGame();

                        } else if (GameInviteOperator.isMyInviteRejected(gameInvite, myPlayer)) {

                            Toast.makeText(getApplicationContext(),
                                    " " + gameInvite.getInvitee().getPlayer().getUsername() + " " + getString(R.string.reject_request), Toast.LENGTH_SHORT)
                                    .show();

                            isGameInviteOperation = true;
                            removeDatabaseListeners();
                            returnUserToWaitingRoom();
                        }

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        gameInviteReference.addValueEventListener(gameInviteEventListener);

    }

    private void click(final Button btn) {

        //if true then it is myPlayer's turn to play, else do nothing
        if (ActiveGameOperator.checkCurrentTurnPlayer(myPlayer, activeGame)) {

            Move move = new Move();

            //if myPlayer is XPlayer then print X, else print O
            if (ActiveGameOperator.checkXPlayer(myPlayer, activeGame)) {

                move.setMovePosition(btn.getTag().toString());
                move.setMoveSymbol(getString(R.string.x));

                //change player turn
                activeGame.setCurrentTurnPlayer(activeGame.getoPlayer().getPlayer());


            } else {

                move.setMovePosition(btn.getTag().toString());
                move.setMoveSymbol(getString(R.string.o));

                //change player turn
                activeGame.setCurrentTurnPlayer(activeGame.getxPlayer().getPlayer());

            }

            activeGame.setMove(move);
            int roundCount = activeGame.getRoundCount();
            activeGame.setRoundCount(++roundCount);

            FirebaseOperator.pushActiveGame(activeGame, this);

        }

    }

    private void updateBoard(Button button, String symbol) {

        if (symbol.equals(getString(R.string.x))) {
            button.setTextColor(getResources().getColor(R.color.colorAccent));
        } else {
            button.setTextColor(getResources().getColor(R.color.colorPrimary));
        }

        button.setText(symbol);

        //users cant click the same button
        button.setEnabled(false);

    }

    private void checkWinner(ActiveGame updatedActiveGame) {

        //Check winner after  round 5
        //Before that nobody can win
        if (updatedActiveGame.getRoundCount() >= 5) {

            //check if there is a winner
            if (isThereAWinner()) {
                announceWinner(updatedActiveGame);
            }
            //if roundCount is 9, then draw
            //else continue;
            else if (updatedActiveGame.getRoundCount() == 9) {
                callTieGame();
            }

        }
    }

    private boolean isThereAWinner() {

        String[][] buttonStatus = new String[3][3];

        buttonStatus[0][0] = btn00.getText().toString();
        buttonStatus[0][1] = btn01.getText().toString();
        buttonStatus[0][2] = btn02.getText().toString();
        buttonStatus[1][0] = btn10.getText().toString();
        buttonStatus[1][1] = btn11.getText().toString();
        buttonStatus[1][2] = btn12.getText().toString();
        buttonStatus[2][0] = btn20.getText().toString();
        buttonStatus[2][1] = btn21.getText().toString();
        buttonStatus[2][2] = btn22.getText().toString();

        //there are eight different positions to end the game
        //3 rows
        //3 column
        //2 diagonal

        //check all rows
        for (int i = 0; i < 3; i++) {
            if (buttonStatus[i][0].equals(buttonStatus[i][1])
                    && buttonStatus[i][0].equals(buttonStatus[i][2])
                    && !buttonStatus[i][0].equals("")) {
                return true;
            }
        }

        //check all columns
        for (int i = 0; i < 3; i++) {
            if (buttonStatus[0][i].equals(buttonStatus[1][i])
                    && buttonStatus[0][i].equals(buttonStatus[2][i])
                    && !buttonStatus[0][i].equals("")) {
                return true;
            }
        }

        //check first diagonal
        if (buttonStatus[0][0].equals(buttonStatus[1][1])
                && buttonStatus[0][0].equals(buttonStatus[2][2])
                && !buttonStatus[0][0].equals("")) {
            return true;
        }

        //check second diagonal
        if (buttonStatus[0][2].equals(buttonStatus[1][1])
                && buttonStatus[0][2].equals(buttonStatus[2][0])
                && !buttonStatus[0][2].equals("")) {
            return true;
        }

        //no winners
        return false;
    }

    private void announceWinner(ActiveGame updatedActiveGame) {

        if (ActiveGameOperator.checkCurrentTurnPlayer(updatedActiveGame.getoPlayer().getPlayer(), updatedActiveGame)) {

            //XPlayer wins
            tvWinner.setText(getResources().getString(R.string.player_online_wins, activeGame.getxPlayer().getPlayer().getUsername()));
            tvWinner.setTextColor(getResources().getColor(R.color.colorAccent));

        } else {
            //OPlayer wins
            tvWinner.setText(getResources().getString(R.string.player_online_wins, activeGame.getoPlayer().getPlayer().getUsername()));
            tvWinner.setTextColor(getResources().getColor(R.color.colorPrimary));

        }

        GameUiOperator.showGameFinishedUi(buttonList, tvPlayAgain, tvWinner);
    }

    private void callTieGame() {

        tvWinner.setText(getResources().getString(R.string.draw));
        tvWinner.setTextColor(getResources().getColor(R.color.tieGame));

        GameUiOperator.showGameFinishedUi(buttonList, tvPlayAgain, tvWinner);

    }

    @OnClick(R.id.tv_play_again)
    public void onTvPlayAgainClicked() {

        GameInvite gameInvite = createNewGameInvite();

        FirebaseDatabase.getInstance().getReference()
                .child(getString(R.string.path_gameInvite)).child(gameInvite.getInvitee().getPlayer().getUserId())
                .setValue(gameInvite);

    }

    private GameInvite createNewGameInvite() {

        Invitee inviteePlayer = new Invitee();
        Inviter inviterPlayer = new Inviter();

        if (ActiveGameOperator.checkXPlayer(myPlayer, activeGame)) {
            inviteePlayer.setPlayer(activeGame.getoPlayer().getPlayer());
            inviterPlayer.setPlayer(activeGame.getxPlayer().getPlayer());
        } else {
            inviteePlayer.setPlayer(activeGame.getxPlayer().getPlayer());
            inviterPlayer.setPlayer(activeGame.getoPlayer().getPlayer());
        }

        return GameInviteOperator.getGameInvite(inviteePlayer, inviterPlayer, InviteStatus.WAITING);

    }

    private void createGameInviteDialog(GameInvite gameInvite) {

        mGameInvite = gameInvite;
        isGameInviteDialogShowed = true;

        FragmentManager fragmentManager = getSupportFragmentManager();

        gameInviteDialog = new GameInviteDialog(this, gameInvite, false);
        fragmentManager.beginTransaction().add(gameInviteDialog, "GameInvite Dialog").commitAllowingStateLoss();
        gameInviteDialog.setCancelable(false);

    }

    @Override
    public void acceptGameInvite(GameInvite gameInvite) {
        //dialog closed
        isGameInviteDialogShowed = false;

        restartActiveGame();
        FirebaseOperator.removeGameInvite(gameInvite, this);

    }

    @Override
    public void rejectGameInvite(GameInvite gameInvite) {
        //dialog closed
        isGameInviteDialogShowed = false;

        Log.d(TAG, "rejectGameInvite: invite rejected by invitee" + gameInvite.getInvitee().getPlayer().getUsername());

        FirebaseOperator.removeGameInvite(gameInvite, this);
        FirebaseOperator.removeActiveGame(activeGame, this);

        isGameInviteOperation = true;
        removeDatabaseListeners();
        returnUserToWaitingRoom();

    }

    private void restartActiveGame() {

        resetGameBoard();

        //Delete previous activeGame
        FirebaseOperator.removeActiveGame(activeGame, this);

        //Create new activeGame and push to Firebase
        activeGame = ActiveGameOperator.changeSides(activeGame, this);
        FirebaseOperator.pushActiveGame(activeGame, this);

        //listen changes with reference
        activeGameReference = FirebaseDatabase.getInstance().getReference()
                .child(getString(R.string.path_activeGame))
                .child(activeGame.getoPlayer().getPlayer().getUserId() + activeGame.getxPlayer().getPlayer().getUserId());

        activeGameReference.addValueEventListener(activeGameEventListener);

        printPlayerNames();

    }

    private void resetGameBoard() {
        GameUiOperator.enableButtons(buttonList);
        GameUiOperator.resetButtonStatus(buttonList);
        GameUiOperator.makeRestartInvisible(tvPlayAgain);
        GameUiOperator.makeWinnerInvisible(tvWinner);
    }

    private void printPlayerNames() {
        tvPlayer1Name.setText(getString(R.string.player1_name, activeGame.getxPlayer().getPlayer().getUsername()));
        tvPlayer2Name.setText(getString(R.string.player2_name, activeGame.getoPlayer().getPlayer().getUsername()));
    }

    @Override
    public void onBackPressed() {
        createGameExitDialog();
    }

    private void createGameExitDialog() {

        FragmentManager fragmentManager = getSupportFragmentManager();
        GameExitDialog gameExitDialog = new GameExitDialog(this);
        fragmentManager.beginTransaction().add(gameExitDialog, "GameExit Dialog").commitAllowingStateLoss();
        gameExitDialog.setCancelable(false);

    }

    @Override
    public void exitGame() {
        returnUserToWaitingRoom();
    }

    @Override
    public void stayGame() {
        //Do nothing
    }

    private void returnUserToWaitingRoom() {
        showInterstitialAd();
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                startActivity(new Intent(OnlineGameActivity.this, WaitingRoomActivity.class));
                finish();
            }

        });

    }

    private void removeDatabaseListeners() {
        activeGameReference.removeEventListener(activeGameEventListener);
        gameInviteReference.removeEventListener(gameInviteEventListener);
    }

    private void getPlayersInfo() {

        Intent intent = getIntent();
        myPlayer = intent.getParcelableExtra((getString(R.string.intent_my_player)));
        activeGame = intent.getParcelableExtra((getString(R.string.intent_active_game)));

    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause: OnlineGame ");

        if (isGameInviteDialogShowed) {

            FirebaseDatabase.getInstance().getReference().child(getString(R.string.path_gameInvite))
                    .child(mGameInvite.getInvitee().getPlayer().getUserId())
                    .child(getString(R.string.path_inviteStatus))
                    .setValue(InviteStatus.REJECTED);

            FirebaseOperator.removeGameInvite(mGameInvite, this);

            gameInviteDialog.dismiss();
            isGameInviteDialogShowed = false;
        }

        if (!isGameInviteOperation) {
            if (amITheLeaver) {
                amITheLeaver = false;
                activeGame.setLeftDuringGame(true);
                FirebaseOperator.pushActiveGame(activeGame, this);
            }
            removeDatabaseListeners();
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG, "onRestart: OnlineGame ");
        returnUserToWaitingRoom();
    }

    private void initiateInterstitialAd() {
        mInterstitialAd = new InterstitialAd(this);
        // when publish change unitId with original
        // originalID: ca-app-pub-8799259961389460/7518905599
        // demoId: ca-app-pub-3940256099942544/1033173712
        mInterstitialAd.setAdUnitId("ca-app-pub-8799259961389460/7518905599");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
    }

    private void showInterstitialAd() {
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        } else {
            Log.d(TAG, getString(R.string.ad_load_failed));
        }
    }

    private void setTagToButtons() {
        btn00.setTag("00");
        btn01.setTag("01");
        btn02.setTag("02");
        btn10.setTag("10");
        btn11.setTag("11");
        btn12.setTag("12");
        btn20.setTag("20");
        btn21.setTag("21");
        btn22.setTag("22");

    }

    private List<Button> addButtonsToList() {

        List<Button> buttonList = new ArrayList<>();
        buttonList.add(btn00);
        buttonList.add(btn01);
        buttonList.add(btn02);
        buttonList.add(btn10);
        buttonList.add(btn11);
        buttonList.add(btn12);
        buttonList.add(btn20);
        buttonList.add(btn21);
        buttonList.add(btn22);

        return buttonList;

    }

    @OnClick(R.id.btn_00)
    public void onBtn00Clicked() {
        btn00.setTag("00");
        click(btn00);
    }

    @OnClick(R.id.btn_01)
    public void onBtn01Clicked() {
        btn01.setTag("01");
        click(btn01);
    }

    @OnClick(R.id.btn_02)
    public void onBtn02Clicked() {
        btn02.setTag("02");
        click(btn02);
    }

    @OnClick(R.id.btn_10)
    public void onBtn10Clicked() {
        btn10.setTag("10");
        click(btn10);
    }

    @OnClick(R.id.btn_11)
    public void onBtn11Clicked() {
        btn11.setTag("11");
        click(btn11);
    }

    @OnClick(R.id.btn_12)
    public void onBtn12Clicked() {
        btn12.setTag("12");
        click(btn12);
    }

    @OnClick(R.id.btn_20)
    public void onBtn20Clicked() {
        btn20.setTag("20");
        click(btn20);
    }

    @OnClick(R.id.btn_21)
    public void onBtn21Clicked() {
        btn21.setTag("21");
        click(btn21);
    }

    @OnClick(R.id.btn_22)
    public void onBtn22Clicked() {
        btn22.setTag("22");
        click(btn22);
    }
}