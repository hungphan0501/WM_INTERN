package com.example.demo.config.jwt;

import com.example.demo.component.TokenCache;
import com.example.demo.dto.DeviceEntType;
import com.example.demo.dto.TokenEntType;
import com.example.demo.dto.TokenResponse;
import com.example.demo.entity.Device;
import com.example.demo.entity.Session;
import com.example.demo.entity.User;
import com.example.demo.repository.DeviceRepository;
import com.example.demo.service.SessionService;
import com.example.demo.service.UserService;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class JwtTokenProvider {

    private final JwtConfig jwtConfig;

    @Autowired
    SessionService sessionService;

    @Autowired
    UserService userService;

    @Autowired
    DeviceRepository deviceRepository;

    @Autowired
    TokenCache tokenCache;

    @Autowired
    public JwtTokenProvider(JwtConfig jwtConfig) {
        this.jwtConfig = jwtConfig;
    }

    public TokenResponse generateToken(DeviceEntType deviceData, String oldToken) {
        String deviceId = deviceData.getDeviceId();
        String deviceType = deviceData.getType();
        Long userId = (deviceData.getUserId() != 0) ? deviceData.getUserId() : 0;

        Date now = new Date();
        Date expirationDate;
        Claims oldTokenClaims = extractAllClaims(oldToken);
        if (oldTokenClaims != null) {
            expirationDate = oldTokenClaims.getExpiration();
        } else {
            // Không có token cũ, tạo mới token với thời hạn mới
            expirationDate = new Date(now.getTime() + jwtConfig.getValidityInMs());
        }
        // Cập nhật thời gian hết hạn của token cũ (gia hạn)
        Claims claims = Jwts.claims()
                .setExpiration(expirationDate)
                .setIssuedAt(now);

        Session session = sessionService.getSessionByDeviceId(deviceId);
        if (session == null) {
            // Tạo phiên mới nếu không tồn tại
            session = new Session();
            session.setDeviceId(deviceId);
            session.setUserId(userId);
            sessionService.createSession(session);
        } else {
            if (userId != 0) {
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
        tokenCache.addToken(token);
        Device device = deviceRepository.findById(deviceId).orElse(null);
        User user = userService.getUserById(userId);
        TokenEntType tokenEntType = new TokenEntType(session.getId().toString(), token, deviceType, expirationDate.getTime(), device, user);

        return new TokenResponse(tokenEntType.getSession(), tokenEntType.getToken(), tokenEntType.getType(), tokenEntType.getExpiredAt(), tokenEntType.getDevice().getDeviceId(), (tokenEntType.getUser() != null ? tokenEntType.getUser().getId() : 0));
    }


    public Claims extractAllClaims(String token) throws JwtException {
        if (token != null && token.startsWith("Bearer ")) {
            String cleanToken = token.replace("Bearer ", "");
            if (validateToken(cleanToken)) {
                return Jwts.parser()
                        .setSigningKey(jwtConfig.getSecretKey())
                        .parseClaimsJws(cleanToken)
                        .getBody();
            }
        }
        return null;
    }

    public boolean validateToken(String token) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(jwtConfig.getSecretKey())
                    .parseClaimsJws(token)
                    .getBody();

            return (claims.getExpiration() == null || claims.getExpiration().before(new Date())) ? false : true;

        } catch (Exception e) {
            return false;
        }
    }

    public Long getUserIdFromToken(String token) {
        try {
            return extractAllClaims(token) != null ? extractAllClaims(token).get("userId", Long.class) : null;
        } catch (Exception e) {
            return null;
        }
    }

    public boolean isAdmin(String token) {
        try {
            Claims claims = extractAllClaims(token);
            Long userId = claims.get("userId", Long.class);

            if (userId != null) {
                User userDetails = userService.getUserById(userId);
                return userDetails != null && userDetails.getRoles().stream()
                        .anyMatch(role -> role.getName().equals("ROLE_ADMIN"));
            }
            return false;
        } catch (ExpiredJwtException e) {
            // Xử lý ngoại lệ token hết hạn
            return false;
        } catch (Exception e) {
            // Xử lý các ngoại lệ khác
            return false;
        }
    }
}

