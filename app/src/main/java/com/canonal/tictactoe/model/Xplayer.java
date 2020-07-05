package com.canonal.tictactoe.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Xplayer implements Parcelable {
    Player player;
    String symbol;

    public Xplayer(){}

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



    protected Xplayer(Parcel in) {
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
    public static final Parcelable.Creator<Xplayer> CREATOR = new Parcelable.Creator<Xplayer>() {
        @Override
        public Xplayer createFromParcel(Parcel in) {
            return new Xplayer(in);
        }

        @Override
        public Xplayer[] newArray(int size) {
            return new Xplayer[size];
        }
    };
}