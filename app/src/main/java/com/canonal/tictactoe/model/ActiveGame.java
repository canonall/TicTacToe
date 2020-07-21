package com.canonal.tictactoe.model;


import android.os.Parcel;
import android.os.Parcelable;

public class ActiveGame implements Parcelable {

    XPlayer xPlayer;
    OPlayer oPlayer;
    Player currentTurnPlayer;
    int roundCount;
    Move move;
    boolean isLeftDuringGame;

    public ActiveGame() {
    }

    public XPlayer getxPlayer() {
        return xPlayer;
    }

    public void setxPlayer(XPlayer xPlayer) {
        this.xPlayer = xPlayer;
    }

    public OPlayer getoPlayer() {
        return oPlayer;
    }

    public void setoPlayer(OPlayer oPlayer) {
        this.oPlayer = oPlayer;
    }

    public Move getMove() {
        return move;
    }

    public void setMove(Move move) {
        this.move = move;
    }

    public int getRoundCount() {
        return roundCount;
    }

    public void setRoundCount(int roundCount) {
        this.roundCount = roundCount;
    }

    public Player getCurrentTurnPlayer() {
        return currentTurnPlayer;
    }

    public void setCurrentTurnPlayer(Player currentTurnPlayer) {
        this.currentTurnPlayer = currentTurnPlayer;
    }

    public boolean isLeftDuringGame() {
        return isLeftDuringGame;
    }

    public void setLeftDuringGame(boolean leftDuringGame) {
        isLeftDuringGame = leftDuringGame;
    }



    protected ActiveGame(Parcel in) {
        xPlayer = (XPlayer) in.readValue(XPlayer.class.getClassLoader());
        oPlayer = (OPlayer) in.readValue(OPlayer.class.getClassLoader());
        currentTurnPlayer = (Player) in.readValue(Player.class.getClassLoader());
        roundCount = in.readInt();
        move = (Move) in.readValue(Move.class.getClassLoader());
        isLeftDuringGame = in.readByte() != 0x00;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(xPlayer);
        dest.writeValue(oPlayer);
        dest.writeValue(currentTurnPlayer);
        dest.writeInt(roundCount);
        dest.writeValue(move);
        dest.writeByte((byte) (isLeftDuringGame ? 0x01 : 0x00));
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<ActiveGame> CREATOR = new Parcelable.Creator<ActiveGame>() {
        @Override
        public ActiveGame createFromParcel(Parcel in) {
            return new ActiveGame(in);
        }

        @Override
        public ActiveGame[] newArray(int size) {
            return new ActiveGame[size];
        }
    };
}