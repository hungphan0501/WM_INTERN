package com.example.demo.controller;

import com.example.demo.config.JwtTokenProvider;
import com.example.demo.dto.LoginResponse;
import com.example.demo.model.User;
import com.example.demo.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.context.WebServerInitializedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestController
public class LoginController implements ApplicationListener<WebServerInitializedEvent> {

    private int port;

    private static final Logger loginLogger = LoggerFactory.getLogger("user-login-logger");

    @Autowired
    UserService userService;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Override
    public void onApplicationEvent(WebServerInitializedEvent webServerInitializedEvent) {
        port=webServerInitializedEvent.getWebServer().getPort();
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody User user) {
        System.out.println("vào được login");
        try {
            User currentUser = userService.getUserByUsername(user.getUsername());
            System.out.println("get user? " + currentUser.toString());
            if (currentUser != null && currentUser.getPassword().equals(user.getPassword())) {
                System.out.println("đăng nhập thành công " );

                String token = jwtTokenProvider.generateToken(currentUser.getUsername());
                loginLogger.info("Đăng nhập thành công: idUser={}, thời gian={}", user.getUsername(), LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
                return ResponseEntity.ok(new LoginResponse(true, "Login successful", token,this.port));
            }
            System.out.println("đăng nhập không thành công " );
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new LoginResponse(false, "Invalid username or password",this.port));
        }  catch (Exception ex) {
            System.out.println("đăng nhập không thành công " );

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new LoginResponse(false, "An error occurred during login",this.port));
        }
    }


    @GetMapping("/test")
    public String test() {
        userService.save(new User("nguyenvanan","1234567890"));
        return "Hello world";
    }

}
