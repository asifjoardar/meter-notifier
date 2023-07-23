package com.asif.meternotifier.service.impl;

import com.asif.meternotifier.entity.MeterAccountDetails;
import com.asif.meternotifier.repository.MeterAccountDetailsRepository;
import com.asif.meternotifier.service.MeterAccountDetailsService;
import org.springframework.stereotype.Service;

@Service
public class MeterAccountDetailsServiceImpl implements MeterAccountDetailsService {
    private final MeterAccountDetailsRepository meterAccountDetailsRepository;

    public MeterAccountDetailsServiceImpl(MeterAccountDetailsRepository meterAccountDetailsRepository) {
        this.meterAccountDetailsRepository = meterAccountDetailsRepository;
    }

    @Override
    public MeterAccountDetails findByAccountNumber(String accountNumber) {
        return meterAccountDetailsRepository.findByAccountNumber(accountNumber);
    }

    @Override
    public void deleteByAccountNumber(String accountNumber) {
        meterAccountDetailsRepository.deleteByAccountNumber(accountNumber);
    }
}
