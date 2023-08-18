package com.example.demo.dto;

public class UserResponse {
    private String username;
    private String email;
    private String fullName;
    private String address;

    public UserResponse(String username,String email, String fullName, String address) {
        this.username=username;
        this.email = email;
        this.fullName = fullName;
        this.address = address;
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

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }


}
