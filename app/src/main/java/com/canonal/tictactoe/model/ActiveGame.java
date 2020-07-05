package com.canonal.tictactoe.model;


import android.os.Parcel;
import android.os.Parcelable;

public class ActiveGame implements Parcelable {
    Xplayer xplayer;
    Oplayer oplayer;


    public ActiveGame(){}

    public Xplayer getXplayer() {
        return xplayer;
    }

    public void setXplayer(Xplayer xplayer) {
        this.xplayer = xplayer;
    }

    public Oplayer getOplayer() {
        return oplayer;
    }

    public void setOplayer(Oplayer oplayer) {
        this.oplayer = oplayer;
    }

    protected ActiveGame(Parcel in) {
        xplayer = (Xplayer) in.readValue(Xplayer.class.getClassLoader());
        oplayer = (Oplayer) in.readValue(Oplayer.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(xplayer);
        dest.writeValue(oplayer);
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