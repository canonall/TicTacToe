package com.canonal.tictactoe.utility;

import android.util.Log;

import com.canonal.tictactoe.model.GameInvite;
import com.canonal.tictactoe.model.Invitee;
import com.canonal.tictactoe.model.Inviter;
import com.canonal.tictactoe.model.Player;

import java.util.List;

public class GameInviteOperator {

    public static Invitee getInviteePlayer(List<Player> playerList, int position) {

        Invitee inviteePlayer = new Invitee();

        Player player = new Player();
        String inviteePlayerId = playerList.get(position).getUserId();
        String inviteePlayerName = playerList.get(position).getUsername();

        player.setUserId(inviteePlayerId);
        player.setUsername(inviteePlayerName);

        inviteePlayer.setPlayer(player);
        return inviteePlayer;

    }

    public static Inviter getInviterPlayer(Player myPlayer) {

        Inviter inviterPlayer = new Inviter();
        inviterPlayer.setPlayer(myPlayer);
        return inviterPlayer;

    }

    public static GameInvite getGameInvite(Invitee invitee, Inviter inviter) {
        GameInvite gameInvite = new GameInvite();
        gameInvite.setInvitee(invitee);
        gameInvite.setInviter(inviter);
        return gameInvite;
    }
    public static boolean isMyPlayerInvited(String inviteePlayerId, String inviteePlayerName, Player myPlayer) {
        if (myPlayer.getUserId().equals(inviteePlayerId) && myPlayer.getUsername().equals(inviteePlayerName)) {
            Log.d("GAME INVITE", "onDataChange-->invite received-->invitee: " + inviteePlayerId + " " + inviteePlayerName);
            return true;
        } else return false;
    }
}
