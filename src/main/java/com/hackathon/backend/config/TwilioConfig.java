package com.hackathon.backend.config;

import com.twilio.Twilio;
import com.twilio.rest.verify.v2.service.Verification;
import com.twilio.rest.verify.v2.service.VerificationCheck;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

@Configuration
@Service
public class TwilioConfig {

    @Value("${ACCOUNT_SID}")
    private String ACCOUNT_SID;

    @Value("${AUTH_TOKEN}")
    private String AUTH_TOKEN;

    @Value("${VERIFICATION_SERVICE_SID}")
    private String VERIFICATION_SERVICE_SID;

    public void sendSms(String phoneNumber){
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
        Verification verification = Verification.creator(
                VERIFICATION_SERVICE_SID,
                phoneNumber,
                "sms"
        ).create();
        System.out.println("Verification SID: " + verification.getSid());
    }

    public boolean checkVerificationCode(String phoneNumber, String code) {
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
        VerificationCheck verificationCheck = VerificationCheck.creator(VERIFICATION_SERVICE_SID)
                .setTo(phoneNumber)
                .setCode(code)
                .create();
        return "approved".equals(verificationCheck.getStatus());
    }
}
