package com.asif.meternotifier.util;

import com.asif.meternotifier.service.EmailService;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class EmailSenderUtil {
    private final EmailService emailService;

    public EmailSenderUtil(EmailService emailService) {
        this.emailService = emailService;
    }

    @Async
    public void send(String to, String subject, String message) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(to);
        mailMessage.setSubject(subject);
        mailMessage.setText(message);
        emailService.sendEmail(mailMessage);
    }
}
