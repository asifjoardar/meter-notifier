package com.asif.meternotifier.service;

import com.asif.meternotifier.entity.Meter;

public interface MeterService {
    void save(Meter meterAccountDetails);

    Meter findByAccountNumber(String accountNumber);

    void deleteByAccountNumber(String accountNumber);
}
