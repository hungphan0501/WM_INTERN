package com.example.demo.config.jwt;

import com.example.demo.dto.DeviceEntType;
import com.example.demo.dto.TokenEntType;
import com.example.demo.dto.UserResponse;
import com.example.demo.model.Session;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.SessionService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class JwtTokenProvider {

    private final JwtConfig jwtConfig;

    @Autowired
    SessionService sessionService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    public JwtTokenProvider(JwtConfig jwtConfig) {
        this.jwtConfig = jwtConfig;
    }

    public TokenEntType generateToken(DeviceEntType deviceData, String oldToken) {
        String deviceId = deviceData.getDeviceId();
        String deviceType = deviceData.getType();
        Long userId = (deviceData.getUserId() != 0) ? deviceData.getUserId() : null;

        Date now = new Date();
        Date expirationDate;

        if (oldToken != null && oldToken.startsWith("Bearer ")) {
            String cleanToken = oldToken.replace("Bearer ", "");
            // Kiểm tra và gia hạn token cũ nếu có
            if (validateToken(cleanToken)) {
                Claims oldTokenClaims = Jwts.parser()
                        .setSigningKey(jwtConfig.getSecretKey())
                        .parseClaimsJws(cleanToken)
                        .getBody();
                expirationDate = oldTokenClaims.getExpiration();
            } else {
                // Token cũ không hợp lệ, tạo mới token với thời hạn mới
                expirationDate = new Date(now.getTime() + jwtConfig.getValidityInMs());
            }
        } else {
            // Không có token cũ, tạo mới token với thời hạn mới
            expirationDate = new Date(now.getTime() + jwtConfig.getValidityInMs());
        }

        // Cập nhật thời gian hết hạn của token cũ (gia hạn)
        Claims claims = Jwts.claims()
                .setExpiration(expirationDate)
                .setIssuedAt(now);


        Session session = sessionService.getSessionByDeviceId(deviceId);
        UserResponse userResponse = null;
        if (session == null) {
            // Tạo phiên mới nếu không tồn tại
            session = new Session();
            session.setDeviceId(deviceId);
            session.setUserId(userId);
            sessionService.createSession(session);
        } else {
            if (userId != null) {
                Optional<User> user = userRepository.findById(userId);
                userResponse = new UserResponse(user.get().getEmail(), user.get().getFullName(), user.get().getAddress());
                session.setUserId(userId);
                sessionService.updateSession(session);
            }
        }

        String token = Jwts.builder()
                .setClaims(claims)
                .setSubject(session.getId().toString())
                .claim("session", session.getId())
                .claim("deviceId", deviceId)
                .claim("deviceType", deviceType)
                .claim("userId", userId)
                .signWith(SignatureAlgorithm.HS256, jwtConfig.getSecretKey())
                .compact();

        return new TokenEntType(session.getId().toString(), token, deviceType, expirationDate.getTime(), deviceId, userResponse);
    }


    public Claims extractAllClaims(String token) throws JwtException {
        if (token != null && token.startsWith("Bearer ")) {
            String cleanToken = token.replace("Bearer ", "");
            return Jwts.parser()
                    .setSigningKey(jwtConfig.getSecretKey())
                    .parseClaimsJws(cleanToken)
                    .getBody();
        }
        return null;

    }

    public boolean validateToken(String token) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(jwtConfig.getSecretKey())
                    .parseClaimsJws(token)
                    .getBody();

            Date expirationDate = claims.getExpiration();
            if (expirationDate == null || expirationDate.before(new Date())) {
                return false; // Token đã hết hạn
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Long getUserIdFromToken(String token) {
        try {
            if (token != null && token.startsWith("Bearer ")) {
                String cleanToken = token.replace("Bearer ", "");
                Claims claims = Jwts.parser().setSigningKey(jwtConfig.getSecretKey()).parseClaimsJws(cleanToken).getBody();
                Long userId = claims.get("userId", Long.class);
                return userId;
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }

}

