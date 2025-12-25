package com.congvo.be_myapp.controller;

import com.congvo.be_myapp.dto.request.ChangePasswordRequest;
import com.congvo.be_myapp.dto.response.UserResponse;
import com.congvo.be_myapp.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/my-info")
    public ResponseEntity<UserResponse> getUserInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        assert authentication != null;
        String currentEmail = authentication.getName();

        UserResponse userResponse = userService.getUserInfo(currentEmail);

        return ResponseEntity.ok(userResponse);
    }

    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(ChangePasswordRequest changePasswordRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentEmail = authentication.getName();
        userService.changePassword(currentEmail, changePasswordRequest);
        return ResponseEntity.ok("Password changed successfully!");
    }

}