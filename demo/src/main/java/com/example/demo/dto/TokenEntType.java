package com.example.demo.dto;

public class TokenEntType {
    private String session;
    private String token;
    private String type;
    private long expiredAt;
    private String DeviceId;
    private UserResponse userLogin;

    public TokenEntType(String session, String token, String type, long expiredAt, String deviceId) {
        this.session = session;
        this.token = token;
        this.type = type;
        this.expiredAt = expiredAt;
        DeviceId = deviceId;
    }

    public TokenEntType(String session, String token, String type, long expiredAt, String deviceId, UserResponse userLogin) {
        this.session = session;
        this.token = token;
        this.type = type;
        this.expiredAt = expiredAt;
        DeviceId = deviceId;
        this.userLogin = userLogin;
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
        return DeviceId;
    }

    public void setDeviceId(String deviceId) {
        DeviceId = deviceId;
    }



    public UserResponse getUserLogin() {
        return userLogin;
    }

    public void setUserLogin(UserResponse userLogin) {
        this.userLogin = userLogin;
    }
}
