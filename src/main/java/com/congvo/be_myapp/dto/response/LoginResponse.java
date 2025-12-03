package com.congvo.be_myapp.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponse {

    private String token;
    private String type = "Bearer";
    private String email;

    public LoginResponse(String token, String email) {
        this.token = token;
        this.email = email;
        this.type = "Bearer";
    }
}
