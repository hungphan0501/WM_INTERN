package com.example.demo.config;

import com.example.demo.config.jwt.JwtTokenFilter;
import com.example.demo.config.jwt.JwtTokenProvider;
import com.example.demo.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.codec.Hex;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

	@Value("${password.secretKey}")
	private String secretKey;
	private final JwtTokenFilter jwtTokenFilter;
	@Autowired
	JwtTokenProvider jwtTokenProvider;

	@Autowired
	public SecurityConfiguration(JwtTokenFilter jwtTokenFilter) {
		this.jwtTokenFilter = jwtTokenFilter;
	}

	@Autowired
	@Lazy
	private UserDetailsServiceImpl userDetailsService;
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new PasswordEncoder() {
			@Override
			public String encode(CharSequence rawPassword) {
				String rawPasswordStr = rawPassword.toString();
				String saltedPassword = rawPasswordStr + secretKey;

				try {
					MessageDigest md = MessageDigest.getInstance("MD5");
					byte[] digest = md.digest(saltedPassword.getBytes());
					return new String(Hex.encode(digest));
				} catch (NoSuchAlgorithmException e) {
					throw new RuntimeException("MD5 algorithm not found");
				}
			}

			@Override
			public boolean matches(CharSequence rawPassword, String encodedPassword) {
				String encodedRawPassword = encode(rawPassword);
				return encodedRawPassword.equals(encodedPassword);
			}
		};
	}
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService)
				.passwordEncoder(passwordEncoder());
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable()
				.authorizeRequests()
				.antMatchers("/api/auth/login", "/api/auth/register","/api/auth/resetPass*/*", "/banner", "/token","/api/auth/wifi/login","/users").permitAll() // Cho phép truy cập API đăng nhập và token
				.antMatchers("/api/admin/**").hasRole("ADMIN")
				.antMatchers("/api/customer/**","/api/project/testCustomerLevel").hasRole("CUSTOMER")

				.antMatchers("/api/info").authenticated()
				.anyRequest().authenticated()
				.and()
				.addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class)
				.exceptionHandling()
				.accessDeniedPage("/403");
	}
}
