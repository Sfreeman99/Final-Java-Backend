package com.example.backend.model;

import java.util.Date;

public class Transactions {
    public int transactionId;
    public String type;
    public Date date;
    public int amount;
    public int reference;

    public Transactions(){}
    public Transactions(int amount, int transactionId, int reference, String type, Date date) {
        this.amount = amount;
        this.transactionId = transactionId;
        this.reference = reference;
        this.type = type;
        this.date = date;
    }
}
