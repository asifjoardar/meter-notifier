package com.asif.meternotifier.service;

import com.asif.meternotifier.entity.MeterAccountDetails;

public interface MeterAccountDetailsService {
    void saveMeterAccountDetails(MeterAccountDetails meterAccountDetails);

    MeterAccountDetails findByAccountNumber(String accountNumber);

    void deleteByAccountNumber(String accountNumber);
}
