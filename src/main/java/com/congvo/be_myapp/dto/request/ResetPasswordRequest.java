package com.congvo.be_myapp.dto.request;

import lombok.Data;

@Data
public class ResetPasswordRequest {

    private String token;
    private String newPassword;

}
