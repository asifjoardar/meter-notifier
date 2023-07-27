package com.asif.meternotifier.service;

import com.asif.meternotifier.entity.Customer;
import com.asif.meternotifier.entity.MeterAccountDetails;
import com.asif.meternotifier.entity.Notification;

public interface MeterAccountDetailsService {
    void save(MeterAccountDetails meterAccountDetails);

    MeterAccountDetails findByAccountNumber(String accountNumber);

    void deleteByAccountNumber(String accountNumber);
}
