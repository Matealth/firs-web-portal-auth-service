package com.firs.risk.mgt.auth.svc.service;

import com.firs.risk.mgt.auth.svc.authresponse.ApiResponse;
import com.firs.risk.mgt.auth.svc.authresponse.LoginResponse;
import com.firs.risk.mgt.auth.svc.entity.User;
import com.firs.risk.mgt.auth.svc.messaging.twilio.TwilioVerification;
import com.firs.risk.mgt.auth.svc.model.OTP;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Locale;
import java.util.Random;
import java.util.ResourceBundle;

@Slf4j
@Service
public class OTPService {
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private RabbitMQService rabbitMQService;

    private TwilioVerification twilioVerification;

    private Locale locale = new Locale("en", "US");
    private ResourceBundle messages = ResourceBundle.getBundle("messages", locale);

    public  void sendTokenToPhone(String phone){
        OTP otp = new OTP(phone, "", "sms", "");
        rabbitMQService.sendOTP(otp);
    }

    public   void sendTokenToEmail(String email){
        OTP otp = new OTP("", email, "email", "");
        rabbitMQService.sendOTP(otp);
    }

    public boolean verifyOTP(OTP otp) {
        twilioVerification = new TwilioVerification();
        return  twilioVerification.checkVerification(otp.getPhone(), otp.getCode());
    }

    public String generatePin(int digits) {
        String pin = "";
        Random passwdIdRandom = new Random();
        String otp = "";
        for (int a = 0; a < digits; a++) {
            int iResult = passwdIdRandom.nextInt(9);
            otp += String.valueOf(iResult);
        }
        return otp;
    }

    // this is temporary

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

}
