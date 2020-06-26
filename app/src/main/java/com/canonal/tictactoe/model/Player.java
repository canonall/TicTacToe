package com.canonal.tictactoe.model;

public class Player {

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
}
