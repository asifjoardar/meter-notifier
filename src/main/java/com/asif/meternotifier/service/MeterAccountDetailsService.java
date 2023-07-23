package com.asif.meternotifier.service;

import com.asif.meternotifier.entity.MeterAccountDetails;

public interface MeterAccountDetailsService {
    public void saveMeterAccountDetails(MeterAccountDetails meterAccountDetails);

    public MeterAccountDetails findByAccountNumber(String accountNumber);

    public void deleteByAccountNumber(String accountNumber);
}
