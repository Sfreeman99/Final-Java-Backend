package com.example.backend;

import org.springframework.web.bind.annotation.RequestBody;

import java.io.UnsupportedEncodingException;
import java.security.SecureRandom;
import java.sql.*;

public class LoginUser {
    public String username;
    public String password;

    public LoginUser(){}
    public LoginUser(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String login(String url) {
        String loginHash = "";
        try (Connection conn = DriverManager.getConnection(url)) {
            PreparedStatement st = conn.prepareStatement("UPDATE CashUser SET loginhash = ? WHERE username = ?");
            SecureRandom random = new SecureRandom();
            byte bytes[] = new byte[20];
            random.nextBytes(bytes);
            String token = new String(bytes, "UTF-8");
            st.setString(1, token);
            st.setString(2, this.username);
            st.executeUpdate();
            st.close();
            PreparedStatement ls = conn.prepareStatement("SELECT loginhash FROM CashUser WHERE username = ?");
            ls.setString(1, this.username);
            ResultSet rs = ls.executeQuery();
            while (rs.next()) {
                //Cash User Hash
                loginHash = rs.getString(1);
            }
            ls.close();
            rs.close();

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return loginHash;
    }
}
