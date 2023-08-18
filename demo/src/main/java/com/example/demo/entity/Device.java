package com.example.demo.entity;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "device")
public class Device {
    @Id
    @Column(name = "device_id")
    private String deviceId;
    @Column(name = "device_name")
    private String deviceName;
    @Column(name = "device_type")
    private String deviceType;
    @Column(name = "ip_address")
    private String ipAddress;
    @Column(name = "mac_address")
    private String macAddress;
    @Column(name = "is_connected")
    private boolean isConnected;
    @ManyToOne
    private Project project;
    @ManyToMany
    private List<User> users;
    // Constructors, getters, setters, toString, etc.

    // Constructors
    public Device() {
    }

    public Device(String deviceId, String deviceName, String deviceType, String ipAddress, String macAddress, boolean isConnected) {
        this.deviceId = deviceId;
        this.deviceName = deviceName;
        this.deviceType = deviceType;
        this.ipAddress = ipAddress;
        this.macAddress = macAddress;
        this.isConnected = isConnected;
    }

    // Getters and setters
    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    public boolean isConnected() {
        return isConnected;
    }

    public void setConnected(boolean connected) {
        isConnected = connected;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    @Override
    public String toString() {
        return "Device{" +
                "deviceId='" + deviceId + '\'' +
                ", deviceName='" + deviceName + '\'' +
                ", deviceType='" + deviceType + '\'' +
                ", ipAddress='" + ipAddress + '\'' +
                ", macAddress='" + macAddress + '\'' +
                ", isConnected=" + isConnected +
                ", project=" + project +
                ", users=" + users +
                '}';
    }
}
