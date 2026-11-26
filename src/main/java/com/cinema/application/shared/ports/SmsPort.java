package com.cinema.application.shared.ports;

public interface SmsPort {
    void send(String phoneNumber, String message);
}
