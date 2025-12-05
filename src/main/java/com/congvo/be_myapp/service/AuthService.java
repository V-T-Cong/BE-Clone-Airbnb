package com.congvo.be_myapp.service;

import com.congvo.be_myapp.dto.request.LoginRequest;
import com.congvo.be_myapp.dto.request.SignUpRequest;
import com.congvo.be_myapp.entity.User;
import com.congvo.be_myapp.repository.UserRepository;
import com.congvo.be_myapp.util.JwtUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;


    public AuthService(JwtUtil jwtUtil, UserRepository userRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager) {
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
    }

    public String register(SignUpRequest signUpRequest) {

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            throw new RuntimeException("Error: Email is already in use!");
        }
        if (userRepository.existsByPhoneNumber(signUpRequest.getPhoneNumber())) {
            throw new RuntimeException("Error: phone number is already in use!");
        }

        User user = new User();
        user.setUsername(signUpRequest.getUsername());
        user.setPhoneNumber(String.valueOf(signUpRequest.getPhoneNumber()));
        user.setEmail(signUpRequest.getEmail());
        user.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));

        userRepository.save(user);

        return "User registered successfully!";
    }

    public String login(LoginRequest loginRequest) {
        // 1. Xác thực bằng AuthenticationManager
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                )
        );

        User user = userRepository.findByEmail(loginRequest.getEmail());

        return jwtUtil.generateToken(String.valueOf(user.getId()), loginRequest.getEmail(), user.getUsername());
    }

    public String generateTokenOnly(String email) {
        User user = userRepository.findByEmail(email);
        return jwtUtil.generateToken(String.valueOf(user.getId()), email, user.getUsername());
    }

}
