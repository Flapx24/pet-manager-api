package com.example.demo.security;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;

@Configuration
public class JwtConfig {
    
    // Default expiration time set to 4 hours (in milliseconds)
    private static final Long DEFAULT_EXPIRATION = 14400000L;
    
    @Value("${jwt.secret}")
    private String secretKey;
    
    @Value("${jwt.expiration:#{null}}")
    private Long expiration;
    @PostConstruct
    public void init() {
        if (secretKey == null || secretKey.trim().isEmpty() || "your_jwt_secret_key".equals(secretKey)) {
            throw new IllegalStateException("The JWT key is not set correctly in application.properties. Please set a secure value for jwt.secret.");
        }
        
        if (expiration == null) {
            expiration = DEFAULT_EXPIRATION;
        }
    }
    
    @Bean
    public SecretKey jwtSecretKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes());
    }
    
    public Long getExpiration() {
        return expiration;
    }
}
