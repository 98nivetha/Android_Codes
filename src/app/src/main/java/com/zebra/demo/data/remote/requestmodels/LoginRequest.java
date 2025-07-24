package com.zebra.demo.data.remote.requestmodels;

public class LoginRequest {
    private String Username;
    private String Password;

    // Constructor
    public LoginRequest(String username, String password) {
        this.Username = username;
        this.Password = password;
    }

    // Getters and setters
    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        this.Username = username;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        this.Password = password;
    }
}

