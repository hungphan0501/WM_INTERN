package com.example.demo.service;

import com.example.demo.entity.Session;
import com.example.demo.repository.SessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SessionService {

    @Autowired
    private SessionRepository sessionRepository;

    public void createSession(Session session) {
        sessionRepository.save(session);
    }

    public Optional<Session> getSession(Long id) {
        return sessionRepository.findById(id);
    }

    public void updateSession(Session session) {
        sessionRepository.save(session);
    }

    public Session getSessionByDeviceId(String deviceId) {
        return sessionRepository.getSessionByDeviceId(deviceId);
    }

//    public List<DeviceEntType> getLoginDevices(Long userId) {
//        List<DeviceEntType> loginDevices = new ArrayList<>();
//        List<Session> sessions = sessionRepository.findByUserIdAndIsLoggedIn(userId, true);
//        for (Session session : sessions) {
//            DeviceEntType device = new DeviceEntType();
//            device.setDeviceId(session.getSessionId());
//            // Thêm thông tin khác của thiết bị vào đây nếu cần
//            loginDevices.add(device);
//        }
//        return loginDevices;
//    }
//
//    public void logoutDevice(String sessionId) {
//        sessionRepository.deleteById(sessionId);
//    }
}

