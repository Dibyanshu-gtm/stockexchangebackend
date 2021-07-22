package com.example.stockexchangebackend.models;

public class LoginResponse {
    private String username;
    private String email;
    private Boolean Admin;

    public LoginResponse(){

    }
    public LoginResponse(String username, String email, Boolean admin) {
        this.username = username;
        this.email = email;
        Admin = admin;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getAdmin() {
        return Admin;
    }

    public void setAdmin(Boolean admin) {
        Admin = admin;
    }
}