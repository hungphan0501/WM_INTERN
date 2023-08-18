package com.example.demo.controller;

import com.example.demo.dto.DeviceDto;
import com.example.demo.dto.ProjectDto;
import com.example.demo.entity.Device;
import com.example.demo.entity.Project;
import com.example.demo.service.DeviceService;
import com.example.demo.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/project")
public class ProjectController {

    @Autowired
    ProjectService projectService;

    @Autowired
    DeviceService deviceService;

    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> createProject(@RequestBody ProjectDto projectDto) {
        try {
            Project project = projectService.findByName(projectDto.getName());
            if (project != null) {
                return ResponseEntity.badRequest().body("Project already exists");
            }
            project = new Project();
            project.setName(projectDto.getName());
            project.setTitle(projectDto.getTitle());
            project.setLogo(projectDto.getLogo());
            projectService.createProject(project);
            return ResponseEntity.ok(project);
        } catch (Exception e) {
            String errorMessage = "An error occurred while creating the project.";
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMessage);
        }
    }

    @PostMapping("/{projectId}/device/create")
    @PreAuthorize("hasRole('ADMIN') or hasRole('CUSTOMER')")
    public ResponseEntity<?> createDevice(@PathVariable("projectId") Long projectId, @RequestBody DeviceDto deviceDto) {
        try {
            Project project = projectService.findById(projectId);
            if (project == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Project not found.");
            }

            if(deviceService.findByName(deviceDto.getDeviceName()) == null) {
                Device device = new Device();
                device.setDeviceId(deviceService.generateUniqueDeviceId());
                device.setDeviceName(deviceDto.getDeviceName());
                device.setDeviceType(deviceDto.getDeviceType());
                device.setIpAddress(deviceDto.getIpAddress());
                device.setMacAddress(deviceDto.getMacAddress());
                device.setConnected(deviceDto.isConnected());
                device.setProject(project);
                deviceService.createDevice(device);
                JSONObject responseJson = new JSONObject();
                responseJson.put("deviceId", device.getDeviceId());
                responseJson.put("deviceName", device.getDeviceName());
                responseJson.put("deviceType", device.getDeviceType());
                responseJson.put("ipAddress", device.getIpAddress());
                responseJson.put("macAddress", device.getMacAddress());
                responseJson.put("projectId", project.getId());
                return ResponseEntity.ok(responseJson.toString());
            }
           return ResponseEntity.badRequest().body("Device already exists");

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while creating the device.");
        }
    }

}
