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
    public void signupUser(@RequestBody User newUser) {
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
    public HashMap<String, String> login(@RequestBody User user) {
        HashMap loginHash = new HashMap();
     try (Connection conn = DriverManager.getConnection(url)) {
         PreparedStatement st = conn.prepareStatement("UPDATE CashUser SET loginhash = ? WHERE username = ?");
         SecureRandom random = new SecureRandom();
         byte bytes[] = new byte[20];
         random.nextBytes(bytes);
         String token = new String(bytes, "UTF-8");
         st.setString(1, token);
         st.setString(2, user.Username);
         st.executeUpdate();
         st.close();
         PreparedStatement ls = conn.prepareStatement("SELECT loginhash FROM CashUser WHERE username = ?");
         ResultSet rs = ls.executeQuery();
         while (rs.next()) {
             //Cash User Hash
             loginHash.put("CuH", rs.getString(1));
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
