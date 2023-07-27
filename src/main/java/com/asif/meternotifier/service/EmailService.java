package com.asif.meternotifier.service;

public interface EmailService {
    void sendEmail(String to, String subject, String message);
}
