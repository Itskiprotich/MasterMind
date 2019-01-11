package com.imejadevs.mastermind.Leaders;

public class UsersList {
    String username,score;

    public UsersList(String username, String score) {
        this.username = username;
        this.score = score;
    }

    public UsersList() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }
}
