package com.example.demo.controller;

import com.example.demo.dto.ProjectDto;
import com.example.demo.entity.Device;
import com.example.demo.entity.Project;
import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.DeviceService;
import com.example.demo.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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


    @DeleteMapping("/delete/{projectId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteProject(@PathVariable("projectId") Long projectId) {
        try {
            String message = "";
            boolean isDelete = false;
            Project project = projectService.findById(projectId);
            if (project != null) {
                projectService.delete(project);
                message = "Delete project successfully";
                isDelete = true;
            } else {
                message = "Project is not exists";
            }
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("projectId", (projectId != null) ? projectId : 0);
            jsonObject.put("message", message);
            jsonObject.put("isDelete", isDelete);
            return ResponseEntity.ok(jsonObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while delete device.");
        }
    }

    @DeleteMapping("/{projectId}/device/delete/{deviceId}")
    @PreAuthorize("hasAnyRole('ADMIN','CUSTOMER')")
    public ResponseEntity<?> deleteDevice(@PathVariable("projectId") Long projectId, @PathVariable("deviceId") String deviceId) {
        try {
            String message = "";
            boolean isDelete = false;
            Project project = projectService.findById(projectId);
            if (project != null) {
                Device device = deviceService.findByDeviceId(deviceId);
                if (device != null) {
                    deviceService.delete(device);
                    message = "Delete device successfully";
                    isDelete = true;
                } else {
                    message = "Device is not exists";
                }
            } else {
                message = "Project is not exists";
            }
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("deviceId", (deviceId != null) ? deviceId : 0);
            jsonObject.put("message", message);
            jsonObject.put("isDelete", isDelete);
            return ResponseEntity.ok(jsonObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while delete device.");
        }
    }

    @Autowired
    UserRepository userRepository;

    @GetMapping("/testCustomerLevel")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<String> test() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            String email = authentication.getName();
            User user = userRepository.findByEmail(email);
            if (user!=null && user.getLevel() >= 3) {
                return ResponseEntity.ok("Bạn được phép thực hiện chức năng này!");
            }
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Bạn bị giới hạn không có quyền truy cập!");
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Bạn cần phải đăng nhập");
    }

}
