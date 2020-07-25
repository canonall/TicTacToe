package com.canonal.tictactoe.utility.operator;

import com.canonal.tictactoe.enums.InviteStatus;
import com.canonal.tictactoe.model.GameInvite;
import com.canonal.tictactoe.model.Invitee;
import com.canonal.tictactoe.model.Inviter;
import com.canonal.tictactoe.model.Player;

import java.util.List;

public class GameInviteOperator {

    public static Invitee getInviteePlayerFromList(List<Player> playerList, int position) {

        Invitee inviteePlayer = new Invitee();

        Player player = new Player();
        String inviteePlayerId = playerList.get(position).getUserId();
        String inviteePlayerName = playerList.get(position).getUsername();

        player.setUserId(inviteePlayerId);
        player.setUsername(inviteePlayerName);

        inviteePlayer.setPlayer(player);
        return inviteePlayer;

    }

    public static Inviter getInviterPlayer(Player player) {

        Inviter inviterPlayer = new Inviter();
        inviterPlayer.setPlayer(player);
        return inviterPlayer;

    }

    public static GameInvite getGameInvite(Invitee invitee, Inviter inviter, InviteStatus inviteStatus) {
        GameInvite gameInvite = new GameInvite();
        gameInvite.setInvitee(invitee);
        gameInvite.setInviter(inviter);
        gameInvite.setInviteStatus(inviteStatus);
        return gameInvite;
    }

    public static boolean isMyPlayerInvited(GameInvite gameInvite, Player myPlayer) {

        String inviteePlayerId = gameInvite.getInvitee().getPlayer().getUserId();
        String inviteePlayerName = gameInvite.getInvitee().getPlayer().getUsername();
        InviteStatus inviteStatus = gameInvite.getInviteStatus();

        return myPlayer.getUserId().equals(inviteePlayerId)
                && myPlayer.getUsername().equals(inviteePlayerName)
                && inviteStatus.equals(InviteStatus.WAITING);
    }

    public static boolean isMyInviteAccepted(GameInvite gameInvite, Player myPlayer) {

        String inviterPlayerId = gameInvite.getInviter().getPlayer().getUserId();
        String inviterPlayerName = gameInvite.getInviter().getPlayer().getUsername();
        InviteStatus inviteStatus = gameInvite.getInviteStatus();

        return myPlayer.getUserId().equals(inviterPlayerId)
                && myPlayer.getUsername().equals(inviterPlayerName)
                && inviteStatus.equals(InviteStatus.ACCEPTED);
    }

    public static boolean isMyInviteRejected(GameInvite gameInvite, Player myPlayer) {

        String inviterPlayerId = gameInvite.getInviter().getPlayer().getUserId();
        String inviterPlayerName = gameInvite.getInviter().getPlayer().getUsername();
        InviteStatus inviteStatus = gameInvite.getInviteStatus();

        return myPlayer.getUserId().equals(inviterPlayerId)
                && myPlayer.getUsername().equals(inviterPlayerName)
                && inviteStatus.equals(InviteStatus.REJECTED);
    }

}
