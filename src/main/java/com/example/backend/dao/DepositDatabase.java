package com.example.backend.dao;

import java.sql.*;

import com.example.backend.model.Deposit;
import com.example.backend.model.Transactions;

public class DepositDatabase {

    public static Transactions deposit(Deposit user, String url){
        Transactions newTransaction = null;
        try (Connection conn = DriverManager.getConnection(url)){
            PreparedStatement st = conn.prepareStatement("UPDATE CashUser SET balance_in_pennies = balance_in_pennies + ? WHERE username = ?");
            st.setInt(1, user.amount);
            st.setString(2, user.username);
            st.executeUpdate();
            st.close();
            PreparedStatement nextSt = conn.prepareStatement("INSERT INTO transactions (user_id, amount, type, reference) VALUES (?,?,?,?)");
            nextSt.setInt(1, user.userId);
            nextSt.setInt(2, user.amount);
            nextSt.setString(3, "deposit");
            nextSt.setInt(4, user.depositId);
            nextSt.executeUpdate();
            nextSt.close();
            PreparedStatement getData = conn.prepareStatement("SELECT * FROM transactions where user_id = ? ORDER BY id DESC");
            getData.setInt(1, user.userId);
            ResultSet currentDeposit = getData.executeQuery();
            currentDeposit.next();
            int transactionId = currentDeposit.getInt("id");
            int amount = currentDeposit.getInt("amount");
            int reference = currentDeposit.getInt("reference");
            String type = currentDeposit.getString("type");
            Date date = currentDeposit.getDate("day");
            newTransaction = new Transactions(amount, transactionId, reference, type, date);
            return newTransaction;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return newTransaction;
    }
}
