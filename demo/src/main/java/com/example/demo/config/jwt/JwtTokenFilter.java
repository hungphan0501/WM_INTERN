package com.example.demo.config.jwt;

import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.List;

@Component
public class JwtTokenFilter extends OncePerRequestFilter {

    @Autowired
    private JwtConfig jwtConfig;

    private final UserDetailsService userDetailsService;
    @Autowired
    UserService userService;

    public JwtTokenFilter(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        String header = request.getHeader("Authorization");

        if (header == null || !header.startsWith("Bearer ")) {
            chain.doFilter(request, response);
            return;
        }

        String token = header.substring(7);

        try {
            if (isValidToken(token)) {
                Claims claims = Jwts.parser().setSigningKey(jwtConfig.getSecretKey()).parseClaimsJws(token).getBody();
                Long userId = claims.get("userId", Long.class);
                Date expirationDate = claims.getExpiration();

                if (expirationDate == null || expirationDate.before(new Date())) {
                    response.setStatus(HttpStatus.UNAUTHORIZED.value());
                    return;
                }

                // Lấy danh sách các đường dẫn không cần xác thực từ bean đã tạo
                List<String> publicUrlsList = jwtConfig.publicUrlsList();

                // Kiểm tra nếu là các API không yêu cầu user login thì cho phép truy cập
                if (publicUrlsList.contains(request.getRequestURI())) {
                    chain.doFilter(request, response);
                    return;
                }
                // Các API cần user login
                if (userId != null) {
                    User userDetails = (User) userDetailsService.loadUserByUsername(userService.getUserById(userId).getUsername());
                    System.out.println("User:   " +userDetails);
                    Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            } else {
                // Handle token validation failure
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                return;
            }
        } catch (Exception e) {
            // Handle any other exceptions
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return;
        }

        chain.doFilter(request, response);
    }

    private boolean isValidToken(String token) {
        try {
            Claims claims = Jwts.parser().setSigningKey(jwtConfig.getSecretKey()).parseClaimsJws(token).getBody();
            Long userId = claims.get("userId", Long.class);
            System.out.println("check token userId: " + userId);

            // Kiểm tra hạn sử dụng của token
            Date expirationDate = claims.getExpiration();
            if (expirationDate == null || expirationDate.before(new Date())) {
                return false;
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }


}
