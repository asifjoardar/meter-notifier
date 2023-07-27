package com.asif.meternotifier.service.impl;

import com.asif.meternotifier.entity.ConfirmationToken;
import com.asif.meternotifier.entity.Customer;
import com.asif.meternotifier.repository.ConfirmationTokenRepository;
import com.asif.meternotifier.service.ConfirmationTokenService;
import com.asif.meternotifier.service.EmailService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

@Service
public class ConfirmationTokenServiceImpl implements ConfirmationTokenService {
    private final ConfirmationTokenRepository confirmationTokenRepository;
    private final EmailService emailService;

    @Value("${host}")
    private String host;

    public ConfirmationTokenServiceImpl(ConfirmationTokenRepository confirmationTokenRepository,
                                        EmailService emailService) {
        this.confirmationTokenRepository = confirmationTokenRepository;
        this.emailService = emailService;
    }

    @Override
    public void generateAndSendToken(Customer customer) {
        // token generate
        ConfirmationToken confirmationToken = new ConfirmationToken();
        confirmationToken.setCustomer(customer);
        confirmationToken.setConfirmationToken(UUID.randomUUID().toString());
        confirmationToken.setCreatedDate(new Date());
        confirmationTokenRepository.save(confirmationToken);
        // email sender
        emailService.sendEmail(customer.getEmail(),
                "Validate Your Email Address - Action Required",
                "To confirm your account, please click here : "
                        + host
                        + "/confirm-account?token="
                        + confirmationToken.getConfirmationToken());
    }

    public ConfirmationToken findByConfirmationToken(String confirmationToken){
        return confirmationTokenRepository.findByConfirmationToken(confirmationToken);
    }
}
