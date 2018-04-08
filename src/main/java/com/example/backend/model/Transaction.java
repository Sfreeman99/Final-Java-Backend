package com.example.backend.model;

public class Transaction {
    public String username;
    public int amount;
    public int userId;
    public int otherId = 7;

    public Transaction(){}
    public Transaction(String username, int amount, int userId) {
        this.username = username;
        this.amount = amount;
        this.userId = userId;
        this.otherId = 7;
    }
}
