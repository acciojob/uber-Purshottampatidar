package com.driver.model;

import javax.persistence.*;
@Entity
@Table
public class Admin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int adminId;
    private String Username;
    private String password;

    public Admin() {}
    public Admin(String UserName, String password) {
        this.Username = UserName;
        this.password = password;
    }

    public int getAdminId() {
        return adminId;
    }

    public void setAdminId(int adminId) {
        this.adminId = adminId;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}