package com.example.demo.dto;

public class LoginResponse {
    private boolean success;
    private String message;
    private String token;
    private int port;

    public LoginResponse(boolean success, String message, String token, int port) {
        this.success = success;
        this.message = message;
        this.token = token;
        this.port = port;
    }

    public LoginResponse(boolean success, String message, int port) {
        this.success = success;
        this.message = message;
        this.port = port;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
