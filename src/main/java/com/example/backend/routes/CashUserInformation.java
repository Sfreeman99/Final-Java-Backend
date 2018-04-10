package com.example.backend.routes;

import com.example.backend.dao.DepositDatabase;
import com.example.backend.dao.WithdrawDatabase;
import com.example.backend.model.*;
import org.springframework.web.bind.annotation.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

@RestController
public class CashUserInformation {
    private static String url = "jdbc:postgresql://localhost/cashit?user=shedliafreeman&password=pgpass";

    @CrossOrigin()
    @PostMapping("/accountSummary")
    public CashUser accountSummary(@RequestBody CashUserAuth sessionKey) {
        HashMap userInfo = new HashMap();
        CashUser user = new CashUser();
        try (Connection conn = DriverManager.getConnection(url)) {
            PreparedStatement st = conn.prepareStatement("Select id, username, first_name, last_name, balance_in_pennies" +
                    " FROM CashUser where loginhash = ? ");
            st.setString(1, sessionKey.sessionKey);
            ResultSet rs = st.executeQuery();


            while (rs.next()){
//                userInfo.put("username", rs.getString(1));
//                userInfo.put("first_name", rs.getString(2));
//                userInfo.put("last_name", rs.getString(3));
//                userInfo.put("balance", rs.getInt(4));
                user.id = rs.getInt("id");
                user.username = rs.getString("username");
                user.firstName = rs.getString("first_name");
                user.lastName = rs.getString("last_name");
                user.balance = rs.getInt("balance_in_pennies");
            }
            rs.close();
            st.close();
        } catch (SQLException e) {
            e.printStackTrace();

        }
        return user;
    }


    @CrossOrigin()
    @PostMapping("/deposit")
    public Transactions deposit( @RequestBody Deposit user){
        return DepositDatabase.deposit(user, url);
    }
    @CrossOrigin()
    @PostMapping("/withdraw")
    public Transactions withdrawal(@RequestBody Withdraw user) {
        return WithdrawDatabase.withdraw(user, url);
    }
    @CrossOrigin()
    @GetMapping("/transactions/{id}")
    public ArrayList<Transactions> transactions(@PathVariable int id){
        ArrayList transactions = new ArrayList();
        try (Connection conn = DriverManager.getConnection(url)) {
            PreparedStatement st = conn.prepareStatement("Select * from transactions where user_id = ?");
            st.setInt(1, id);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                Transactions transaction;
                int transactionId = rs.getInt("id");
                String type = rs.getString("type");
                Date date = rs.getDate("day");
                int amount = rs.getInt("amount");
                int reference = rs.getInt("reference");
                transaction = new Transactions(amount, transactionId, reference, type, date);
                transactions.add(transaction);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return transactions;
    }
}
