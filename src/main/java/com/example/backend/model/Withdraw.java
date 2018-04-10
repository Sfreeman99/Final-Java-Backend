package com.example.backend.model;

public class Withdraw {
    public String username;
    public int amount;
    public int userId;
    public int withdrawId = 7;

    public Withdraw() {}
    public Withdraw(String username, int amount, int userId) {
        this.userId = userId;
        this.username = username;
        this.amount = amount;
    }
}
