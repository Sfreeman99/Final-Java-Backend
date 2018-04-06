package com.example.backend.dao;

import org.mindrot.jbcrypt.BCrypt;

import java.sql.*;

public class LoginUserAuthentication {
    public String username;
    public String password;


    public LoginUserAuthentication() {}
    public LoginUserAuthentication(String username, String password) {
        this.username = username;
        this.password = password;
    }

        public boolean usernameExists(String url) {
            int count = 0;
            try (Connection conn = DriverManager.getConnection(url)) {
                PreparedStatement st = conn.prepareStatement("SELECT COUNT(username) FROM CashUser WHERE username = ? ");
                st.setString(1, username);
                ResultSet rs = st.executeQuery();
                while (rs.next()) {
                    count += rs.getInt(1);
                }
                rs.close();
                st.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            if (count > 0)
                return true;
            else
                return false;
        }
        public boolean passwordMatch(String salt, String url) {
            String password = "";
            try (Connection conn = DriverManager.getConnection(url)) {
                PreparedStatement st = conn.prepareStatement("SELECT passwordhash FROM CashUser WHERE username = ?");
                st.setString(1, this.username);
                ResultSet rs = st.executeQuery();
                while (rs.next()) {
                    password = rs.getString(1);
                    System.out.println("Passwordhash: " + password);
                }
                String givenPassword = BCrypt.hashpw(this.password, salt);
                System.out.println("Given: " + givenPassword);
                if (givenPassword.equals(password))
                    return true;
                else
                    return false;
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }


        }


    }
