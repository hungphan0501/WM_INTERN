package com.example.demo.dto;

import com.example.demo.entity.User;

import java.util.Date;

public class UserRegister {
    private UserResponse user;
    private Date createAt;
    private boolean statusCreate;

    public UserResponse getUser() {
        return user;
    }

    public void setUser(UserResponse user) {
        this.user = user;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }

    public boolean isStatusCreate() {
        return statusCreate;
    }

    public void setStatusCreate(boolean statusCreate) {
        this.statusCreate = statusCreate;
    }

    public UserRegister(UserResponse user, Date createAt, boolean statusCreate) {
        this.user = user;
        this.createAt = createAt;
        this.statusCreate = statusCreate;
    }
}
