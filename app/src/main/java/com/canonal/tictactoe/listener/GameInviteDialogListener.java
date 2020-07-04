package com.canonal.tictactoe.listener;

import com.canonal.tictactoe.model.Player;

public interface GameInviteDialogListener {
    void createMatch(Player inviteePlayer, Player inviterPlayer);
}
