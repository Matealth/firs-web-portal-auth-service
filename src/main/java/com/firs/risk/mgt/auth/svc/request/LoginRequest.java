package com.firs.risk.mgt.auth.svc.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class LoginRequest {

    @JsonProperty("email")
    @NotNull(message="email is required")
    private String email;

    @JsonProperty("password")
    @NotNull(message="password is required")
    private String password;

    @JsonProperty("otp_code")
    private String otpCode;

}
