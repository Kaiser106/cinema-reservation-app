package com.cinema.application.port;

public interface SmsPort {
    void send(String phoneNumber, String message);
}
