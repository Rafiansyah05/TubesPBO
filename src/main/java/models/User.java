package models;

import interfaces.GenerateID;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;


public class User extends JDBC implements GenerateID {

 
    private String idUser;
    private String name;
    private String email;
    private String password;
    private String role;

    public User() {
    }

    public User(String name, String email, String password) {
        this.name     = name;
        this.email    = email;
        this.password = password;
    }

    public User(String idUser, String name, String email, String password, String role) {
        this.idUser   = idUser;
        this.name     = name;
        this.email    = email;
        this.password = password;
        this.role     = role;
    }

    @Override
    public String generateID() {
        return UUID.randomUUID().toString();
    }


    public boolean login(String email, String password) {
        try {
            connect();
            if (conn == null) return false;
            String sql = "SELECT * FROM users WHERE email = ? AND password = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, email);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                this.idUser   = rs.getString("id_user");
                this.name     = rs.getString("name");
                this.email    = rs.getString("email");
                this.password = rs.getString("password");
                this.role     = rs.getString("role");
                return true;
            }
        } catch (SQLException e) {
            System.out.println("Error login: " + e.getMessage());
        } finally {
            disconnect();
        }
        return false;
    }

 
    public void logout() {

        this.idUser   = null;
        this.name     = null;
        this.email    = null;
        this.password = null;
        this.role     = null;
    }

    public boolean validateEmail(String email) {
  
        return email != null && email.contains("@") && email.contains(".");
    }

  
    public boolean updatePassword(String newPassword) {
        try {
            connect();
            if (conn == null) return false;
            String sql = "UPDATE users SET password = ? WHERE id_user = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, newPassword);
            ps.setString(2, this.idUser);
            int rows = ps.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            System.out.println("Error update password: " + e.getMessage());
            return false;
        } finally {
            disconnect();
        }
    }

    
    @Override
    public String getProfile() {
        return "ID: " + idUser + ", Nama: " + name + ", Email: " + email + ", Role: " + role;
    }

    public String getIdUser()   { return idUser; }
    public void setIdUser(String idUser) { this.idUser = idUser; }

    public String getName()     { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail()    { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getRole()     { return role; }
    public void setRole(String role) { this.role = role; }
}
