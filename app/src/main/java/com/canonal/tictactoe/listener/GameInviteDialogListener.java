package com.canonal.tictactoe.listener;

import com.canonal.tictactoe.model.GameInvite;
import com.canonal.tictactoe.model.Player;

public interface GameInviteDialogListener {
    void acceptGameInvite(GameInvite gameInvite);
    void rejectGameInvite(GameInvite gameInvite);
}
