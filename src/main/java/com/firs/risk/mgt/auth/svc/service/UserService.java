package com.firs.risk.mgt.auth.svc.service;

import com.firs.risk.mgt.auth.svc.authresponse.ApiResponse;
import com.firs.risk.mgt.auth.svc.authresponse.LoginResponse;
import com.firs.risk.mgt.auth.svc.entity.Role;
import com.firs.risk.mgt.auth.svc.entity.User;
import com.firs.risk.mgt.auth.svc.enums.RoleEnum;
import com.firs.risk.mgt.auth.svc.model.OTP;
import com.firs.risk.mgt.auth.svc.repo.RoleRepo;
import com.firs.risk.mgt.auth.svc.repo.UserRepo;
import com.firs.risk.mgt.auth.svc.security.userdetails.UserDetailsServiceImpl;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    private final OTPService otpService;
    private final RoleRepo roleRepo;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    private Locale locale = new Locale("en", "US");
    private ResourceBundle messages = ResourceBundle.getBundle("messages", locale);

    public ApiResponse verifyOTP(OTP input) {
        ApiResponse apiResponse = null;
        HashMap<String, String> result = new HashMap<>();
        User user = userDetailsService.findByPhone(input.getPhone()).orElseThrow();
        OTP otp = new OTP(user.getPhone(), "",  "sms", input.getCode());
        boolean verificationResponse  = otpService.verifyOTP(otp);
        if (verificationResponse){
            user.setActive(true);
            result.put("status", "activated");
            apiResponse = ApiResponse.builder()
                    .success(true)
                    .message(messages.getString("user-activated"))
                    .build();
        } else {
            apiResponse = ApiResponse.builder()
                    .success(false)
                    .message(messages.getString("wrong-otp"))
                    .build();
        }
        return  apiResponse;
    }

    public List<User> getUsers() {
        return userRepo.findAll();
    }

    public ApiResponse logout(String username) {
        User user = userDetailsService.findByUsername(username).orElseThrow();;
        Date now = new Date(System.currentTimeMillis());
        if (user != null) {
            user.setLastLogin(now);
        }
        ApiResponse apiResponse = ApiResponse.builder()
                .success(true)
                .message(messages.getString("logout-success"))
                .build();
        return apiResponse;
    }


    public ApiResponse generateToken(String phone) {
        ApiResponse apiResponse;
        User user = userDetailsService.findByPhone(phone).orElseThrow();
        log.error("New User {} {}", phone, user);
        if ( user != null) {
            log.error("Masked: {}", phone.substring(0, 6) + "****" + phone.substring(10));
            otpService.sendTokenToPhone(phone);
            apiResponse = ApiResponse.builder()
                    .success(true)  //  +2348057790564
                    .message(messages.getString("enter-token") + phone.substring(0, 6) + "****" + phone.substring(10))
                    // .message()
                    .build();
        } else {
            apiResponse = ApiResponse.builder()
                    .success(false)
                    .message(messages.getString("user-not-found"))
                    .build();
        }
        return apiResponse;
    }
    public ApiResponse map(User user, String jwt){
        LoginResponse loginResponse = LoginResponse.builder()
                .firstname(user.getFirstname())
                .lastname(user.getLastname())
                .lastLogin(user.getLastLogin())
                .phone(user.getPhone())
                .email(user.getEmail())
                .jwt(jwt)
                .build();

        ApiResponse apiResponse  = ApiResponse.builder()
                .success(true)
                .result(loginResponse)
                .build();
        return apiResponse;
    }

    public boolean checkNewVsConfirm(String newField, String confirmField){
        if (newField.equalsIgnoreCase(confirmField)) {
            return true;
        }
        return false;
    }

    public User saveUserFromUpload(User user, String role, String bankId){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user = userRepo.save(user);
        addRoleToUser(user.getUsername(), RoleEnum.valueOf(role), bankId);
        return user;
    }

    public void addRoleToUser(String username, RoleEnum roleName, String bankId) {
        User user = userDetailsService.findByUsername(username).orElseThrow();
        Role role = roleRepo.findRoleByName(roleName.getValue());
        user.getRoles().add(role);
    }

    public void removeRoleFromUser(String username, RoleEnum roleName, String bankId){
        User user = userDetailsService.findByUsername(username).orElseThrow();
        Role role = roleRepo.findRoleByName(roleName.getValue());
        Collection<Role> roles = user.getRoles();
        roles.removeIf(x->x.getName() == roleName.getValue());
    }

}

