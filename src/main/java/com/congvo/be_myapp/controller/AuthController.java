package com.congvo.be_myapp.controller;

import com.congvo.be_myapp.dto.request.LoginRequest;
import com.congvo.be_myapp.dto.request.RefreshTokenRequest;
import com.congvo.be_myapp.dto.request.SignUpRequest;
import com.congvo.be_myapp.dto.response.LoginResponse;
import com.congvo.be_myapp.entity.RefreshToken;
import com.congvo.be_myapp.service.AuthService;
import com.congvo.be_myapp.service.RefreshTokenService;
import com.congvo.be_myapp.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final UserService userService;
    private final RefreshTokenService refreshTokenService;

    public AuthController(AuthService authService, UserService userService, RefreshTokenService refreshTokenService) {
        this.authService = authService;
        this.userService = userService;
        this.refreshTokenService = refreshTokenService;
    }

    @PostMapping("/signup")
    public ResponseEntity<String> registerUser(@RequestBody SignUpRequest signUpRequest) {
        try {
            String response = authService.register(signUpRequest);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginRequest loginRequest) {
        try {
            String accessToken = authService.login(loginRequest);
            UUID uuid = userService.getUserID(loginRequest.getEmail());
            String refreshToken = refreshTokenService.getRefreshToken(uuid);
            return ResponseEntity.ok(new LoginResponse(accessToken, refreshToken, loginRequest.getEmail()));
        } catch (Exception e) {
            return ResponseEntity.status(401).body("Invalid email or password");
        }
    }

    @RequestMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(@RequestBody RefreshTokenRequest request) {
        String requestRefreshToken = request.getRefreshToken();

        return refreshTokenService.findByToken(requestRefreshToken)
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUser)
                .map(user -> {
                    String newAccessToken = authService.generateTokenOnly(user.getEmail());
                    String newRefreshToken = refreshTokenService.getRefreshToken(user.getId());
                    return ResponseEntity.ok(new LoginResponse(newAccessToken, newRefreshToken, user.getEmail()));
                })
                .orElseThrow(() -> new RuntimeException("Refresh token is not in database!"));
    }

}
