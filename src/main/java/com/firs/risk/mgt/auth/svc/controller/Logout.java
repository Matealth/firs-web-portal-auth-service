package com.firs.risk.mgt.auth.svc.controller;

import java.util.HashMap;

import com.firs.risk.mgt.auth.svc.authresponse.ApiResponse;
import com.firs.risk.mgt.auth.svc.service.UserService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/logout")

public class Logout {

    private final UserService userService;

    @PostMapping()
    public ResponseEntity<ApiResponse> logout(@RequestBody HashMap<String, String> request){
        return ResponseEntity.ok().body(userService.logout(request.get("username")));
    }


}
