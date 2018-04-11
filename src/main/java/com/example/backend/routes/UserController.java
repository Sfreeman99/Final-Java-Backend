package com.example.backend.routes;

import com.example.backend.dao.LoginUser;
import com.example.backend.dao.LoginUserAuthentication;
import com.example.backend.model.SignupUser;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.sql.*;

@RestController
public class UserController {
    private static String url = "jdbc:postgresql://localhost/cashit?user=shedliafreeman&password=pgpass";

    @Value("${app.salt}")
    private String salt;

    @CrossOrigin()
    @PostMapping("/signup")
    public String signupUser(@RequestBody SignupUser newUser) {
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
        LoginUser user = new LoginUser(newUser.Username, newUser.Password);
        return user.login(url);
    }

    @CrossOrigin()
    @PostMapping("/loginAuthentication")
    public boolean loginAuthentication(@RequestBody LoginUserAuthentication user) {
        if (user.usernameExists(url)) {
            System.out.println("Username is there");
            if (user.passwordMatch(salt,url)) {
                System.out.println("Password Match");
                return true;
            } else {
                System.out.println("Username match but Password is incorrect");
                return false;
            }
        }
        else {
            System.out.println("It isn\'t there");
            return false;
    }
    }

    @CrossOrigin()
    @PostMapping("/login")
    public String login(@RequestBody LoginUser user) {
        return user.login(url);
    }

    @CrossOrigin()
    @PostMapping("/usernameexists")
    public boolean usernameExists(@RequestBody String username) {
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

    @CrossOrigin()
    @PostMapping("/logout")
    public void logout(@RequestBody String loginhash) {
        try (Connection conn = DriverManager.getConnection(url)) {
            PreparedStatement st = conn.prepareStatement("UPDATE CashUser SET loginhash = NULL WHERE loginhash = (?)");
            st.setString(1, loginhash);
            st.executeUpdate();
            st.close();
            System.out.println("goodbye");

    } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @CrossOrigin()
    @DeleteMapping("/delete/{id}")
    public void deleteAccount(@PathVariable int id) throws SQLException{
        try (Connection conn = DriverManager.getConnection(url)) {
            PreparedStatement st = conn.prepareStatement("DELETE FROM cashuser where id = ? ");
            st.setInt(1, id);
            st.execute();
        }
    }
}
