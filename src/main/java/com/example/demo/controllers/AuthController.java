package com.example.demo.controllers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.LoginRequestDTO;
import com.example.demo.dto.RegisterRequestDTO;
import com.example.demo.entities.User;
import com.example.demo.repositories.UserRepository;
import com.example.demo.security.JwtTokenUtil;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtil jwtTokenUtil;
    private final AuthenticationManager authenticationManager;

    public AuthController(UserRepository userRepository, PasswordEncoder passwordEncoder,
            JwtTokenUtil jwtTokenUtil, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenUtil = jwtTokenUtil;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(@Valid @RequestBody RegisterRequestDTO registerRequest) {
        Map<String, Object> result = new HashMap<>();

        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            result.put("success", false);
            result.put("message", "El email ya está registrado");
            return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
        }

        User newUser = new User(
                registerRequest.getName(),
                registerRequest.getSurname(),
                registerRequest.getEmail(),
                passwordEncoder.encode(registerRequest.getPassword()));

        userRepository.save(newUser);

        result.put("success", true);
        result.put("message", "Registro exitoso");
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@Valid @RequestBody LoginRequestDTO loginRequest) {
        Map<String, Object> result = new HashMap<>();

        try {

            User user = userRepository.findByEmail(loginRequest.getEmail())
                    .orElseThrow(() -> new BadCredentialsException("Credenciales erróneas"));

            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            user.getId().toString(),
                            loginRequest.getPassword()));

            String token = jwtTokenUtil.generateToken(user.getId());

            result.put("name", user.getName());
            result.put("token", token);
            result.put("success", true);
            result.put("message", "Login exitoso");

            return new ResponseEntity<>(result, HttpStatus.OK);

        } catch (BadCredentialsException e) {
            result.put("success", false);
            result.put("message", "Credenciales erróneas");
            return new ResponseEntity<>(result, HttpStatus.UNAUTHORIZED);
        }
    }
}
