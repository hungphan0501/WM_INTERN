package com.example.demo.entity;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "device")
public class Device {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "device_id")
    private String deviceId;
    @OneToOne
    private User user;
    @Column(name = "device_name")
    private String deviceName;
    @Column(name = "os")
    private String os;
    @Column(name = "os_version")
    private String osVersion;
    @Column(name = "app_version")
    private String appVersion;
    @Column(name = "network")
    private String network;
    @Column(name = "type")
    private String type;
    @Column(name = "status")
    private String status;
    @Column(name = "create_at")
    private Date createdAt;
    @Column(name = "update_at")
    private Date updatedAt;
    @Column(name = "delete_at")
    private Date deletedAt;
    @Column(name = "expired_at")
    private Date expiredAt;

    @ManyToOne
    private Project project;

    public Device() {
    }

    public Device(String deviceId, User user, String deviceName, String os, String osVersion, String appVersion, String network, String type, String status, Date createdAt, Date updatedAt, Date deletedAt, Date expiredAt, Project project) {
        this.deviceId = deviceId;
        this.user = user;
        this.deviceName = deviceName;
        this.os = os;
        this.osVersion = osVersion;
        this.appVersion = appVersion;
        this.network = network;
        this.type = type;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deletedAt = deletedAt;
        this.expiredAt = expiredAt;
        this.project = project;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
    }

    public String getOsVersion() {
        return osVersion;
    }

    public void setOsVersion(String osVersion) {
        this.osVersion = osVersion;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public String getNetwork() {
        return network;
    }

    public void setNetwork(String network) {
        this.network = network;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Date getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(Date deletedAt) {
        this.deletedAt = deletedAt;
    }

    public Date getExpiredAt() {
        return expiredAt;
    }

    public void setExpiredAt(Date expiredAt) {
        this.expiredAt = expiredAt;
    }
}
