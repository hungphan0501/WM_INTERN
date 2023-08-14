package com.example.demo.dto;

import com.example.demo.model.User;

import java.util.Date;

public class UserRegister {
    private User user;
    private Date createAt;
    private boolean statusCreate;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
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

    public UserRegister(User user, Date createAt, boolean statusCreate) {
        this.user = user;
        this.createAt = createAt;
        this.statusCreate = statusCreate;
    }
}
