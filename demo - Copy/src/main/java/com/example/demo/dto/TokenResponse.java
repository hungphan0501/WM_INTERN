package com.example.demo.dto;

public class TokenResponse {
    private String session;
    private String token;
    private String type;
    private long expiredAt;
    private String deviceId;
    private long userId;

    public TokenResponse(String session, String token, String type, long expiredAt, String deviceId, long userId) {
        this.session = session;
        this.token = token;
        this.type = type;
        this.expiredAt = expiredAt;
        this.deviceId = deviceId;
        this.userId = userId;
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

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }
}
