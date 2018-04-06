package com.example.backend.model;

public class CashUser {
    public int id;
    public String username;
    public String firstName;
    public String lastName;
    public int balance;

    public CashUser() {}
    public CashUser(String sessionKey){
        this.id = id;
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.balance = balance;
    }

    public int getBalance() {
        return balance;
    }

    public int getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getUsername() {
        return username;
    }
}
