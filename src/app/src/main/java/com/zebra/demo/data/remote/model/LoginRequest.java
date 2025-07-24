package com.zebra.demo.data.remote.model;

public class LoginRequest {
    private String Username;
    private String Password;

    public LoginRequest() {}

    public LoginRequest(String username, String password) {
        this.Username = username;
        this.Password = password;
    }

    public String getUsername() { return Username; }
    public void setUsername(String username) { this.Username = username; }

    public String getPassword() { return Password; }
    public void setPassword(String password) { this.Password = password; }

    @Override
    public String toString() {
        return "LoginRequest{" +
                "Username='" + Username + '\'' +
                ", Password='" + Password + '\'' +
                '}';
    }
}
