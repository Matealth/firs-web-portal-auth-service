package com.firs.risk.mgt.auth.svc.controller;

import java.util.*;

import jakarta.servlet.http.HttpServletRequest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.firs.risk.mgt.auth.svc.authresponse.ApiResponse;
import com.firs.risk.mgt.auth.svc.request.LoginRequest;
import com.firs.risk.mgt.auth.svc.service.LoginService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/login")
@Slf4j
public class Login {

    private final LoginService loginService;
    private Locale locale = new Locale("en", "NG");
    private ResourceBundle messages = ResourceBundle.getBundle("messages", locale);

    @PostMapping()
    public ResponseEntity<ApiResponse> login(@RequestBody LoginRequest loginRequest) {
        return loginService.login(loginRequest);
    }

    @PostMapping("/{token}")
    public ResponseEntity<ApiResponse> checkToken(HttpServletRequest request, @PathVariable String token) {
        return loginService.checkToken(request, token);
    }
}
