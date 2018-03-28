package com.example.backend;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.security.SecureRandom;
import java.sql.*;
import java.util.HashMap;

@RestController
public class UserController {
    private static String url = "jdbc:postgresql://localhost/cashit?user=shedliafreeman&password=pgpass";

    @Value("${app.salt}")
    private String salt;

    @CrossOrigin()
    @PostMapping("/signup")
    public void signupUser(@RequestBody SignupUser newUser) {
        try (Connection conn = DriverManager.getConnection(url)) {
            PreparedStatement st = conn.prepareStatement("INSERT INTO CashUser " +
                    "(username, first_name, last_name, email, passwordhash) " +
                    "VALUES (?, ?, ?, ?, ?)");
            st.setString(1, newUser.Username);
            st.setString(2, newUser.FirstName);
            st.setString(3, newUser.LastName);
            st.setString(4, newUser.Email);
            st.setString(5, BCrypt.hashpw(newUser.Password, salt));

            int rowadded = st.executeUpdate();
            System.out.println(rowadded + " rows added");
            st.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @CrossOrigin()
    @PostMapping("/login")
    public String login(@RequestBody LoginUser user) {
        String loginHash = "";
     try (Connection conn = DriverManager.getConnection(url)) {
         PreparedStatement st = conn.prepareStatement("UPDATE CashUser SET loginhash = ? WHERE username = ?");
         SecureRandom random = new SecureRandom();
         byte bytes[] = new byte[20];
         random.nextBytes(bytes);
         String token = new String(bytes, "UTF-8");
         st.setString(1, token);
         st.setString(2, user.username);
         st.executeUpdate();
         st.close();
         PreparedStatement ls = conn.prepareStatement("SELECT loginhash FROM CashUser WHERE username = ?");
         ls.setString(1, user.username);
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

    @CrossOrigin()
    @PostMapping("/usernameexists")
    public boolean usernameExists(@RequestBody String username) {
        int count = 0;
        try (Connection conn = DriverManager.getConnection(url)) {
            System.out.println(username);
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
        System.out.print(count);
        if (count > 0)
            return true;
        else
            return false;
    }

    @CrossOrigin()
    @PostMapping("/logout")
    public void logout(@RequestParam String username) {
        try (Connection conn = DriverManager.getConnection(url)) {
            PreparedStatement st = conn.prepareStatement("UPDATE CashUser SET loginhash = NULL WHERE username = (?)");
            st.setString(1, username);
            st.executeUpdate();
            st.close();

    } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
