package com.canonal.tictactoe.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Oplayer implements Parcelable {
    Player player;
    String symbol;


    public Oplayer(){}

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



    protected Oplayer(Parcel in) {
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
    public static final Parcelable.Creator<Oplayer> CREATOR = new Parcelable.Creator<Oplayer>() {
        @Override
        public Oplayer createFromParcel(Parcel in) {
            return new Oplayer(in);
        }

        @Override
        public Oplayer[] newArray(int size) {
            return new Oplayer[size];
        }
    };
}