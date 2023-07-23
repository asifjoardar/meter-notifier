package com.asif.meternotifier.service.impl;

import com.asif.meternotifier.entity.MeterAccountDetails;
import com.asif.meternotifier.exception.MeterNotFoundException;
import com.asif.meternotifier.repository.MeterAccountDetailsRepository;
import com.asif.meternotifier.service.MeterAccountDetailsService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MeterAccountDetailsServiceImpl implements MeterAccountDetailsService {
    private final MeterAccountDetailsRepository meterAccountDetailsRepository;

    public MeterAccountDetailsServiceImpl(MeterAccountDetailsRepository meterAccountDetailsRepository) {
        this.meterAccountDetailsRepository = meterAccountDetailsRepository;
    }

    @Override
    public void saveMeterAccountDetails(MeterAccountDetails meterAccountDetails) {
        meterAccountDetailsRepository.save(meterAccountDetails);
    }

    @Override
    public MeterAccountDetails findByAccountNumber(String accountNumber) {
        Optional<MeterAccountDetails> meterAccountDetails = meterAccountDetailsRepository.findByAccountNumber(accountNumber);
        meterAccountDetails.orElseThrow(() -> new MeterNotFoundException("Account no: " + accountNumber + " is not found"));
        return meterAccountDetails.get();
    }

    @Override
    public void deleteByAccountNumber(String accountNumber) {
        Optional<MeterAccountDetails> meterAccountDetails = meterAccountDetailsRepository.findByAccountNumber(accountNumber);
        meterAccountDetails.orElseThrow(() -> new MeterNotFoundException("Account no: " + accountNumber + " is not found"));
        meterAccountDetailsRepository.deleteByAccountNumber(accountNumber);
    }
}
