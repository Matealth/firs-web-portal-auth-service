package com.firs.risk.mgt.auth.svc.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;


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
