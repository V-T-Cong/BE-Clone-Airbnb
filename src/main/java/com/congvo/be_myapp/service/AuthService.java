package com.congvo.be_myapp.service;

import com.congvo.be_myapp.dto.request.SignUpRequest;
import com.congvo.be_myapp.entity.User;
import com.congvo.be_myapp.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
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

}
