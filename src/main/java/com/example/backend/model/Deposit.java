package com.example.backend.model;

public class Deposit {
    public String username;
    public int amount;
    public int userId;
    public int depositId = 7;

    public Deposit() {}
    public Deposit(String username, int amount, int userId) {
        this.userId = userId;
        this.username = username;
        this.amount = amount;
    }
}
