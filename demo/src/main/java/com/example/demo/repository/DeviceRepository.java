package com.example.demo.repository;

import com.example.demo.entity.Device;
import com.example.demo.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeviceRepository extends JpaRepository<Device,String> {

    Device findByDeviceName(String name);

    boolean existsByDeviceId(String id);

    List<Device> getAllByProject(Project project);

    Device findByDeviceId(String id);
}
