package com.example.demo.repository;

import com.example.demo.entity.Device;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeviceRepository extends JpaRepository<Device,String> {

    Device findByDeviceName(String name);

    boolean existsByDeviceId(String id);
}
