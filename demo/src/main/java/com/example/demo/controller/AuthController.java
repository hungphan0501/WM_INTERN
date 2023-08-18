package com.example.demo.controller;

import com.example.demo.config.jwt.JwtTokenProvider;
import com.example.demo.dto.*;
import com.example.demo.entity.Device;
import com.example.demo.entity.Session;
import com.example.demo.entity.User;
import com.example.demo.service.DeviceService;
import com.example.demo.service.SessionService;
import com.example.demo.service.UserService;
import io.jsonwebtoken.Claims;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private static final Logger loginLogger = LoggerFactory.getLogger("user-login-logger");

    @Autowired
    UserService userService;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    SessionService sessionService;

    @Autowired
    DeviceService deviceService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserRequest user, @RequestHeader(value = "Authorization", required = false) String token) {
        try {
            Claims claims = jwtTokenProvider.extractAllClaims(token);

            Long sessionId = claims.get("session", Long.class);
            String deviceId = claims.get("deviceId", String.class);
            String deviceType = claims.get("deviceType", String.class);

            Optional<Session> session = sessionService.getSession(sessionId);
            Session currentSession = null;
            if (session.isPresent()) {
                currentSession = session.get();

            }
            if (currentSession != null) {
                User currentUser = userService.getUserByUsername(user.getUsername());
                if (currentUser != null && currentUser.getPassword().equals(user.getPassword())) {
                    // Kiểm tra vai trò của currentUser
                    boolean isUser = SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
                            .anyMatch(auth -> auth.getAuthority().equals("ROLE_USER"));
                    if (!isUser) {
                        DeviceEntType data = new DeviceEntType();
                        data.setUserId(currentUser.getId());
                        data.setDeviceId(deviceId);
                        data.setType(deviceType);
                        TokenResponse newToken = jwtTokenProvider.generateToken(data, token);
                        loginLogger.info("Đăng nhập hệ thống thành công: idUser={}, thời gian={}", user.getUsername(), LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
                        return ResponseEntity.ok(newToken);
                    } else {
                        // Người dùng có vai trò USER, trả về lỗi
                        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
                    }
                }
            }
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("wrong device login");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred during login");
        }
    }

    @PostMapping("/wifi/login")
    public ResponseEntity<?> loginWithWifi(@RequestHeader(value = "Authorization", required = false) String token, @RequestBody UserRequest user) {
        try {
            Claims claims = jwtTokenProvider.extractAllClaims(token);

            Long sessionId = claims.get("session", Long.class);
            String deviceId = claims.get("deviceId", String.class);
            String deviceType = claims.get("deviceType", String.class);

            Optional<Session> session = sessionService.getSession(sessionId);
            Session currentSession = null;
            if (session.isPresent()) {
                currentSession = session.get();

            }
            if (currentSession != null) {
                User currentUser = userService.getUserByUsername(user.getUsername());
                if (currentUser != null && currentUser.getPassword().equals(user.getPassword())) {
                    DeviceEntType data = new DeviceEntType();
                    data.setUserId(currentUser.getId());
                    data.setDeviceId(deviceId);
                    data.setType(deviceType);
                    TokenResponse newToken = jwtTokenProvider.generateToken(data, token);

                    Device device = deviceService.findById(deviceId);
                    List<User> users = device.getUsers();
                    if (!users.contains(currentUser)) {
                        users.add(currentUser);
                        device.setUsers(users);
                        deviceService.updateDevice(device);
                    }
                    loginLogger.info("Đăng nhập wifi thành công: idUser={}, thời gian={}", user.getUsername(), LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
                    return ResponseEntity.ok(newToken);
                }
            }
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("wrong device login");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred during login");
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> test(@RequestBody User user) {
        try {
            User currentUser = userService.getUserByUsername(user.getUsername());
            if (currentUser == null) {
                if (!user.getUsername().equals("") && !user.getPassword().equals("")) {
                    Date date = new Date();
                    User user1 = userService.save(new User(user.getEmail(), user.getFullName(), user.getUsername(), user.getPassword(), user.getAddress()));
                    UserRegister ur = new UserRegister(user1, date, true);
                    return ResponseEntity.ok(ur);
                }
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Username and password cannot be empty");
            }
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Username already exists");
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred during register");
        }
    }

}
