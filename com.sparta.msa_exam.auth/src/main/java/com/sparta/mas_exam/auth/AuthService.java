package com.sparta.mas_exam.auth;

import com.sparta.mas_exam.auth.domain.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
public class AuthService {

    @Value("${spring.application.name}")
    private String issuer;

    @Value("${service.jwt.access-expiration}")
    private Long accessExpiration;

    private final SecretKey secretKey;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(@Value("${service.jwt.secret-key}") String secretKey,
                       UserRepository userRepository,
                       PasswordEncoder passwordEncoder) {
        this.secretKey = Keys.hmacShaKeyFor(Decoders.BASE64URL.decode(secretKey));
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public String createAccessToken(String username, String role) {
        return Jwts.builder()
                .claim("user_name", username)
                .claim("role", role)
                .issuer(issuer)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + accessExpiration))
                .signWith(secretKey, io.jsonwebtoken.SignatureAlgorithm.HS512)
                .compact();
    }

    public User signUp(String username, String password) {
        String encodedPassword = passwordEncoder.encode(password);
        User user = User.builder()
                .username(username)
                .password(encodedPassword)
                .build();
        return userRepository.save(user);
    }

    public String signIn(String username, String password) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user ID or password"));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new IllegalArgumentException("Invalid user ID or password");
        }

        return createAccessToken(user.getUsername(), user.getRole());
    }
}