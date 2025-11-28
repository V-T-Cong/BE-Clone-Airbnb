package com.congvo.be_myapp.dto.request;

import lombok.Data;

@Data
public class SignUpRequest {
    private String username;
    private String phoneNumber;
    private String email;
    private String password;
}
