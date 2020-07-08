package com.canonal.tictactoe.model;

import com.canonal.tictactoe.enums.InviteStatus;

public class GameInvite {
    Invitee invitee;
    Inviter inviter;
    InviteStatus inviteStatus;

    public Invitee getInvitee() {
        return invitee;
    }

    public void setInvitee(Invitee invitee) {
        this.invitee = invitee;
    }

    public Inviter getInviter() {
        return inviter;
    }

    public void setInviter(Inviter inviter) {
        this.inviter = inviter;
    }

    public InviteStatus getInviteStatus() {
        return inviteStatus;
    }

    public void setInviteStatus(InviteStatus inviteStatus) {
        this.inviteStatus = inviteStatus;
    }
}
