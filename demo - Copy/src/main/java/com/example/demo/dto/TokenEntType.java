package com.example.demo.dto;

import com.example.demo.entity.Device;
import com.example.demo.entity.User;

public class TokenEntType {
    private String session;
    private String token;
    private String type;
    private long expiredAt;
    private Device device;
    private User user;

    public TokenEntType(String session, String token, String type, long expiredAt, Device device, User user) {
        this.session = session;
        this.token = token;
        this.type = type;
        this.expiredAt = expiredAt;
        this.device = device;
        this.user = user;
    }

    public Device getDevice() {
        return device;
    }

    public void setDevice(Device device) {
        this.device = device;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getSession() {
        return session;
    }

    public void setSession(String session) {
        this.session = session;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getExpiredAt() {
        return expiredAt;
    }

    public void setExpiredAt(long expiredAt) {
        this.expiredAt = expiredAt;
    }

    public User getUser() {
        return user;
    }

}
