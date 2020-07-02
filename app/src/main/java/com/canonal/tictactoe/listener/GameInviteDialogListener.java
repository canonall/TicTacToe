package com.canonal.tictactoe.listener;

import com.canonal.tictactoe.model.Player;

public interface GameInviteDialogListener {
    void sendGameInvite(boolean isAccepted, Player player);
}
