package com.canonal.tictactoe.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Move implements Parcelable {

    private String movePosition;
    private String moveSymbol;

    public Move() {
    }

    public String getMovePosition() {
        return movePosition;
    }

    public void setMovePosition(String movePosition) {
        this.movePosition = movePosition;
    }

    public String getMoveSymbol() {
        return moveSymbol;
    }

    public void setMoveSymbol(String moveSymbol) {
        this.moveSymbol = moveSymbol;
    }

    protected Move(Parcel in) {
        movePosition = in.readString();
        moveSymbol = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(movePosition);
        dest.writeString(moveSymbol);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Move> CREATOR = new Parcelable.Creator<Move>() {
        @Override
        public Move createFromParcel(Parcel in) {
            return new Move(in);
        }

        @Override
        public Move[] newArray(int size) {
            return new Move[size];
        }
    };
}