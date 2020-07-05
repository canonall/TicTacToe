package com.canonal.tictactoe.model;

import android.os.Parcel;
import android.os.Parcelable;


public class Player implements Comparable<Player>, Parcelable {

    private String userId;
    private String username;

    public Player() {
        this.userId = "No ID";
        this.username = "No Name";
    }


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public int compareTo(Player player) {
        return this.getUsername().compareTo(player.getUsername());
    }


    protected Player(Parcel in) {
        userId = in.readString();
        username = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(userId);
        dest.writeString(username);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Player> CREATOR = new Parcelable.Creator<Player>() {
        @Override
        public Player createFromParcel(Parcel in) {
            return new Player(in);
        }

        @Override
        public Player[] newArray(int size) {
            return new Player[size];
        }
    };
}