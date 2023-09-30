package com.firs.risk.mgt.auth.svc.service;

import java.util.*;

import jakarta.servlet.http.HttpServletRequest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.firs.risk.mgt.auth.svc.authresponse.ApiResponse;
import com.firs.risk.mgt.auth.svc.entity.Tokenizer;
import com.firs.risk.mgt.auth.svc.entity.User;
import com.firs.risk.mgt.auth.svc.helper.AuthUtil;
import com.firs.risk.mgt.auth.svc.helper.Util;
import com.firs.risk.mgt.auth.svc.jwt.JwtUtil;
import com.firs.risk.mgt.auth.svc.model.OTP;
import com.firs.risk.mgt.auth.svc.request.LoginRequest;
import com.firs.risk.mgt.auth.svc.security.userdetails.UserDetailsServiceImpl;

@Slf4j
@Service
@RequiredArgsConstructor
public class LoginService {

    private final UserService userService;
    private final UserDetailsServiceImpl userDetailsService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final TokenizerService tokenizerService;
    private Locale locale = new Locale("en", "NG");
    private ResourceBundle messages = ResourceBundle.getBundle("messages", locale);

    public ResponseEntity<ApiResponse> login(LoginRequest loginRequest) {

        ApiResponse apiResponse;
        User user = userDetailsService
                .findByUsername(loginRequest.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException(messages.getString("login-failure")));

        HashMap<String, Object> result = new HashMap<>();

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);

            if (user.getActive()) {
                if (loginRequest.getOtpCode() == null) {
                    userService.generateToken(user.getPhone());
                    result.put("token-sent", messages.getString("enter-token"));
                    apiResponse = ApiResponse.builder()
                            .success(true)
                            .message(messages.getString("enter-token"))
                            .result(result)
                            .build();
                } else {
                    OTP otp = new OTP(user.getPhone(), "", "sms", loginRequest.getOtpCode());
                    apiResponse = userService.verifyOTP(otp);
                    if (apiResponse.getSuccess()) {
                        Tokenizer tokenizer = tokenizerService.findByPhoneOrEmail(user.getPhone(), user.getEmail());
                        if (tokenizer == null)
                            tokenizer = new Tokenizer();
                        tokenizer.setPhone(user.getPhone());
                        tokenizer.setEmail(user.getEmail());
                        Tokenizer tokenizer1;
                        String token;
                        do {
                            token = Util.generateAlphaNum(50);
                            tokenizer1 = tokenizerService.findByToken(token);
                            tokenizer.setToken(token);
                        } while (tokenizer1 != null);
                        Calendar calendar = Calendar.getInstance();
                        calendar.add(Calendar.MINUTE, 30);
                        tokenizer.setExpireAt(calendar.getTime());
                        tokenizerService.save(tokenizer);
                        String jwt = jwtUtil.generateJwtToken(authentication);
                        result.put("token", token);
                        result.put("jwt", jwt);
                        apiResponse = ApiResponse.builder()
                                .success(true)
                                .message(messages.getString("otp-verified"))
                                .result(result)
                                .build();
                    }
                }
            } else {
                apiResponse = ApiResponse.builder()
                        .success(false)
                        .message(messages.getString("not-activated-or-enabled"))
                        .build();
            }

        } catch (BadCredentialsException ex) {
            apiResponse = ApiResponse.builder()
                    .success(false)
                    .message(messages.getString("invalid-credentials"))
                    .build();
            return ResponseEntity.ok().body(apiResponse);
        }
        return ResponseEntity.ok().body(apiResponse);
    }

    public ResponseEntity<ApiResponse> checkToken(HttpServletRequest request, String token) {
        String headerAuth = request.getHeader("Authorization");
        Authentication authentication = AuthUtil.getPrincipal();
        String userId = authentication.getName();
        User user = userDetailsService.findByUsername(userId).orElseThrow(() -> new UsernameNotFoundException(messages.getString("login-failure")));
        ApiResponse apiResponse = null;

        if (user != null) {
            Tokenizer tokenizer = tokenizerService.findByToken(token);
            Date currentDate = Calendar.getInstance().getTime();
            if (tokenizer != null && currentDate.before(tokenizer.getExpireAt())) {
                apiResponse = userService.map(user, headerAuth);
                apiResponse.setSuccess(true);
                tokenizer.setToken(null);
                tokenizer.setExpireAt(new Date());
                tokenizerService.save(tokenizer);
                apiResponse = userService.map(user, headerAuth);
            } else {
                apiResponse = ApiResponse.builder()
                        .success(false)
                        .message(messages.getString("tokenizer-expired"))
                        .build();
            }
        } else {
            apiResponse = ApiResponse.builder()
                    .success(false)
                    .message(messages.getString("login-failure"))
                    .build();
        }
        return ResponseEntity.ok().body(apiResponse);
    }

}

