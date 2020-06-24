package com.canonal.tictactoe.model;

public class Player {
    private String userId;

    public Player(){
        this.userId="NoID";
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
