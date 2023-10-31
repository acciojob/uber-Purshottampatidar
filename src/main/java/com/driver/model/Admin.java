package com.driver.model;

import javax.persistence.*;
@Entity
@Table
public class Admin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String userName;
    private String password;





    public int getId() {
        return id;
    }



    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    public Admin(){}
    public Admin(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }
}
