package com.example.demo.controller;

import com.example.demo.config.jwt.JwtTokenProvider;
import com.example.demo.dto.UserResponse;
import com.example.demo.entity.Device;
import com.example.demo.entity.Project;
import com.example.demo.entity.User;
import com.example.demo.service.DeviceService;
import com.example.demo.service.ProjectService;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("/api")
public class MainController {
    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @Autowired
    UserService userService;

    @Autowired
    ProjectService projectService;

    @Autowired
    DeviceService deviceService;

    @GetMapping("/info")
    public ResponseEntity<?> getUserInfo(@RequestHeader(value = "Authorization") String token) {
        Long userId = jwtTokenProvider.getUserIdFromToken(token);
        if (userId != null) {
            User user = userService.getUserById(userId);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
            UserResponse userResponse = new UserResponse(user.getUsername(), user.getEmail(), user.getFullName(), user.getAddress());
            return ResponseEntity.ok(userResponse);
        }
        return ResponseEntity.badRequest().body("error");

    }

    @GetMapping("/project/{projectId}/users")
    public ResponseEntity<?> getAllUserUseWifiOfProject(@PathVariable("projectId") Long projectId) {
        try {
            Project project = projectService.findById(projectId);
            List<UserResponse> users = new ArrayList<>();
            if (project != null) {
                List<Device> deviceList = deviceService.getAllDeviceOfProject(project);
                if (deviceList != null) {
                    for (Device device : deviceList) {
                        users.add(new UserResponse(device.getUser().getUsername(), device.getUser().getEmail(), device.getUser().getFullName(), device.getUser().getAddress()));
                    }
                }
            }
            return ResponseEntity.ok(users);
        }  catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("There was an error getting all the users using the project's wifi.");
        }
    }

}
