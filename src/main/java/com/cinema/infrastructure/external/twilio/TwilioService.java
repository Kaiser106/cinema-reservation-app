package com.cinema.infrastructure.external.twilio;

import com.cinema.application.port.SmsPort;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class TwilioService implements SmsPort {
    private static final Logger log = LoggerFactory.getLogger(TwilioService.class);
    private final TwilioConfiguration configuration;

    public TwilioService(TwilioConfiguration configuration) {
        this.configuration = configuration;
    }

    @Override
    public void send(String phoneNumber, String message) {
        if (configuration.getAccountSid() == null || configuration.getAccountSid().isBlank()) {
            log.info("Twilio not configured; simulated SMS to {}", phoneNumber);
            return;
        }
        Twilio.init(configuration.getAccountSid(), configuration.getAuthToken());
        Message.creator(new PhoneNumber(phoneNumber), new PhoneNumber(configuration.getPhoneNumber()), message).create();
    }
}
