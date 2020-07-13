package com.canonal.tictactoe.model;

import android.os.Parcel;
import android.os.Parcelable;

public class OPlayer implements Parcelable {
    Player player;
    String symbol;


    public OPlayer(){}

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



    protected OPlayer(Parcel in) {
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
    public static final Parcelable.Creator<OPlayer> CREATOR = new Parcelable.Creator<OPlayer>() {
        @Override
        public OPlayer createFromParcel(Parcel in) {
            return new OPlayer(in);
        }

        @Override
        public OPlayer[] newArray(int size) {
            return new OPlayer[size];
        }
    };
}