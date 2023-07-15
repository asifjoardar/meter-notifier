package com.asif.meternotifier.util;

import com.asif.meternotifier.service.EmailService;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

@Service
public class EmailSender {
    private EmailService emailService;

    public EmailSender(EmailService emailService){
        this.emailService = emailService;
    }
    public void send(String to, String subject, String message){
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(to);
        mailMessage.setSubject(subject);
        mailMessage.setText(message);
        //emailService.sendEmail(mailMessage);
    }
}
