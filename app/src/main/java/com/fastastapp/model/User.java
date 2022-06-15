package com.fastastapp.model;

import java.util.Collection;

public class User {

    private int id;

    private String username;

    private String email;

    private String password;
    private UserRole userRole;

    private Collection<Test> tests;



    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }






}