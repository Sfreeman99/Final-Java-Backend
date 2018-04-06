package com.example.backend.routes;

import com.example.backend.model.CashUser;
import com.example.backend.model.CashUserAuth;
import com.example.backend.model.Transaction;
import org.springframework.web.bind.annotation.*;

import java.sql.*;
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
    public void deposit( @RequestBody Transaction user){
        try (Connection conn = DriverManager.getConnection(url)){
//            PreparedStatement addToTransactions = conn.prepareStatement("INSERT INTO transactions (user_id, amount, ");
            PreparedStatement st = conn.prepareStatement("UPDATE CashUser SET balance_in_pennies = balance_in_pennies + ? WHERE username = ?");
            st.setInt(1, user.amount);
            st.setString(2, user.username);
            st.executeUpdate();
            st.close();
            PreparedStatement nextSt = conn.prepareStatement("INSERT INTO transactions (user_id, amount, reference) VALUES (?,?,?)");
            nextSt.setInt(1, user.userId);
            nextSt.setInt(2, user.amount);
            nextSt.setInt(3, user.otherId);
            nextSt.executeUpdate();
            nextSt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @CrossOrigin()
    @PostMapping("/withdraw")
    public void withdrawal(@RequestBody Transaction user) {
        try (Connection conn = DriverManager.getConnection(url)) {
            PreparedStatement st = conn.prepareStatement("UPDATE CashUser SET balance_in_pennies = balance_in_pennies - ? WHERE username = ?");
            st.setInt(1, user.amount);
            st.setString(2, user.username);
            st.executeUpdate();
            st.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
