package com.canonal.tictactoe.model;

import android.os.Parcel;
import android.os.Parcelable;

public class XPlayer implements Parcelable {
    Player player;
    String symbol;

    public XPlayer() {
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    protected XPlayer(Parcel in) {
        player = (Player) in.readValue(Player.class.getClassLoader());
        symbol = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(player);
        dest.writeString(symbol);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<XPlayer> CREATOR = new Parcelable.Creator<XPlayer>() {
        @Override
        public XPlayer createFromParcel(Parcel in) {
            return new XPlayer(in);
        }

        @Override
        public XPlayer[] newArray(int size) {
            return new XPlayer[size];
        }
    };
}