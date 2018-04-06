package com.example.backend.model;

public class Transaction {
    public String username;
    public int amount;
    public int userId;
    public int otherId;

    public Transaction(){}
    public Transaction(String username, int amount, int userId, int otherId) {
        this.username = username;
        this.amount = amount;
        this.userId = userId;
        this.otherId = otherId;
    }
}
