package com.asif.meternotifier.service;

import com.asif.meternotifier.entity.ConfirmationToken;
import com.asif.meternotifier.entity.Customer;

public interface ConfirmationTokenService {
    void generateAndSendToken(Customer customer);

    ConfirmationToken findByConfirmationToken(String confirmationToken);
}
