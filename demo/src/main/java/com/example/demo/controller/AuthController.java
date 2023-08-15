package com.example.demo.controller;

import com.example.demo.config.jwt.JwtTokenProvider;
import com.example.demo.dto.LoginResponse;
import com.example.demo.dto.UserRegister;
import com.example.demo.model.User;
import com.example.demo.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private static final Logger loginLogger = LoggerFactory.getLogger("user-login-logger");

    @Autowired
    UserService userService;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody User user) {
        try {
            User currentUser = userService.getUserByUsername(user.getUsername());
            System.out.println("get user? " + currentUser.toString());
            if (currentUser != null && currentUser.getPassword().equals(user.getPassword())) {
                String token = jwtTokenProvider.generateToken(currentUser.getUsername());
                loginLogger.info("Đăng nhập thành công: idUser={}, thời gian={}", user.getUsername(), LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
                return ResponseEntity.ok(new LoginResponse(true, "Login successful", token));
            }
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new LoginResponse(false, "Invalid username or password"));
        }  catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new LoginResponse(false, "An error occurred during login"));
        }
    }


    @PostMapping("/register")
    public ResponseEntity<?> test(@RequestBody User user) {
        try {
            User currentUser = userService.getUserByUsername(user.getUsername());
            if(currentUser == null){
                if(!user.getUsername().equals("") && !user.getPassword().equals("")){
                    Date date = new Date();
                    userService.save(new User(user.getUsername(),user.getPassword()));
                    UserRegister ur = new UserRegister(user,date,true);
                    return ResponseEntity.ok(ur);
                }
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Username and password cannot be empty");
            }
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Username already exists");
        }catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred during register");
        }
    }

}
