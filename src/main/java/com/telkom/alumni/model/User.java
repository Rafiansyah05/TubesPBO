package com.telkom.alumni.model;

import javax.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "users")
public abstract class User implements Searching {
    
    @Id
    private String idUser;
    
    @Column(nullable = false)
    private String name;
    
    @Column(unique = true, nullable = false)
    private String email;
    
    @Column(nullable = false)
    private String password;
    
    public User() {}
    
    public User(String idUser, String name, String email, String password) {
        this.idUser = idUser;
        this.name = name;
        this.email = email;
        this.password = password;
    }
    
    public String getIdUser() { return idUser; }
    public void setIdUser(String idUser) { this.idUser = idUser; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    
    public void login() {
        System.out.println("Login berhasil: " + name);
    }
    
    public void logout() {
        System.out.println("Logout berhasil: " + name);
    }
    
    public abstract String getProfile();
    
    @Override
    public void cekKeyword(String keyword) {
        if (this.name.toLowerCase().contains(keyword.toLowerCase())) {
            System.out.println("Keyword ditemukan pada nama: " + this.name);
        } else {
            System.out.println("Keyword tidak ditemukan pada nama: " + this.name);
        }
    }
}