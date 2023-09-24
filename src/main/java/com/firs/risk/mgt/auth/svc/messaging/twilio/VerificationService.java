package com.firs.risk.mgt.auth.svc.messaging.twilio;

public interface VerificationService {
    void startVerification(String recipient, String channel);
    boolean checkVerification(String recipient, String code);
}