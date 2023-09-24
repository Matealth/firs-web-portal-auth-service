package com.firs.risk.mgt.auth.svc.authresponse;

import lombok.Builder;
import lombok.Data;

import java.util.Date;
import java.util.UUID;

@Data
@Builder
public class LoginResponse {
    private String firstname;
    private String lastname;
    private String email;
    private String phone;
    private String address;
    private Date lastLogin;
    private byte[] image;
    private String jwt;
}
