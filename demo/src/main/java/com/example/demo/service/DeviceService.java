package com.example.demo.service;

import com.example.demo.entity.Device;
import com.example.demo.repository.DeviceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class DeviceService {
    @Autowired
    DeviceRepository deviceRepository;

    @Transactional
    public Device createDevice(Device device) {
        return deviceRepository.save(device);
    }

    @Transactional
    public Device updateDevice(Device device) {
        return deviceRepository.save(device);
    }

    public Device findById(String id) {
        return deviceRepository.findById(id).orElse(null);
    }

    public Device findByName(String name) {
        return deviceRepository.findByDeviceName(name);
    }
    public String generateUniqueDeviceId() {
        String deviceId;
        do {
            deviceId = UUID.randomUUID().toString().substring(0, 8);
        } while (deviceRepository.existsByDeviceId(deviceId));
        return deviceId;
    }

}
