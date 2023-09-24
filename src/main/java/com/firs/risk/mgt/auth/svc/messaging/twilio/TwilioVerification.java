package com.firs.risk.mgt.auth.svc.messaging.twilio;

import com.twilio.Twilio;
import com.twilio.rest.verify.v2.service.Verification;
import com.twilio.rest.verify.v2.service.VerificationCheck;

public class TwilioVerification implements VerificationService {

    private static final String ACCOUNT_SID = "AC48701570009c78305f1817a80c798138";
    private static final String AUTH_TOKEN = "b63c74054b389f3dd178d679af016945";
    private static final String VERIFICATION_SID = "VA0f68c5f7656c34ef91dd4d7d97e3f250";

    public TwilioVerification() {
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
    }

    public void startVerification(String recipient, String channel) {
        Verification.creator(VERIFICATION_SID, recipient, channel).create();
    }

    public boolean checkVerification(String recipient, String code) {
        VerificationCheck verificationCheck = VerificationCheck.creator(
                VERIFICATION_SID,
                code)
                .setTo(recipient).create();
        return "approved".equals(verificationCheck.getStatus());
    }
}