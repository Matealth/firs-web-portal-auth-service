package com.firs.risk.mgt.auth.svc.controller;

import com.firs.risk.mgt.auth.svc.authresponse.ApiResponse;
import com.firs.risk.mgt.auth.svc.entity.Tokenizer;
import com.firs.risk.mgt.auth.svc.entity.User;
import com.firs.risk.mgt.auth.svc.helper.AuthUtil;
import com.firs.risk.mgt.auth.svc.helper.Util;
import com.firs.risk.mgt.auth.svc.jwt.JwtUtil;
import com.firs.risk.mgt.auth.svc.model.OTP;
import com.firs.risk.mgt.auth.svc.request.LoginRequest;
import com.firs.risk.mgt.auth.svc.security.userdetails.UserDetailsServiceImpl;
import com.firs.risk.mgt.auth.svc.service.TokenizerService;
import com.firs.risk.mgt.auth.svc.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/login")
@Slf4j
public class LoginController {

    private final UserService userService;
    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private TokenizerService tokenizerService;


    private Locale locale = new Locale("en", "NG");
    private ResourceBundle messages = ResourceBundle.getBundle("messages", locale);


    @PostMapping()
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {

        ApiResponse apiResponse;
        User user = userDetailsService.findByEmail(loginRequest.getEmail());
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
//                UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            if (user != null) {
                if (user.getActive() && user.getActive()) {
                    if (loginRequest.getOtpCode() == null) {
                        apiResponse = userService.generateToken(user.getPhone());
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
                            // apiResponse = userService.map(user, jwt);
                            HashMap<String, Object> result = new HashMap<>();
                            result.put("token", token);
                            result.put("jwt", jwt);
                            apiResponse = ApiResponse.builder()
                                    .success(true)
                                    .message(messages.getString("otp-verified"))
                                    .result(result)
                                    .build();
                        }
                    }
                }  else {
                    apiResponse = ApiResponse.builder()
                            .success(false)
                            .message(messages.getString("not-activated-enabled"))
                            .build();
                }
            } else {
                apiResponse = ApiResponse.builder()
                        .success(false)
                        .message(messages.getString("login-failure"))
                        .build();
                return ResponseEntity.ok().body(apiResponse);
            }
        } catch (BadCredentialsException ex) {
            log.info(" this happened ");
            apiResponse = ApiResponse.builder()
                    .success(false)
                    .message(messages.getString("invalid-credentials"))
                    .build();
            return ResponseEntity.ok().body(apiResponse);
        }
        return ResponseEntity.ok().body(apiResponse);
    }

    @PostMapping("/{token}")
    public ResponseEntity<?> loginWithToken(HttpServletRequest request, @PathVariable String token ) {
        String headerAuth = request.getHeader("Authorization");
        log.debug(" logging the the auth {} ========> 1 ");
        Authentication authentication = AuthUtil.getPrincipal();
        log.debug(" logging the the auth {} ========> ",  authentication.getName());
        String userId = authentication.getName();
        User user = userDetailsService.findByEmail(userId);
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
        return  ResponseEntity.ok().body(apiResponse);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestBody HashMap<String, String> request){
        return ResponseEntity.ok().body(userService.logout(request.get("email")));
    }



}
