package com.asif.meternotifier.service.impl;

import com.asif.meternotifier.entity.Customer;
import com.asif.meternotifier.entity.MeterAccountDetails;
import com.asif.meternotifier.entity.Notification;
import com.asif.meternotifier.exception.MeterNotFoundException;
import com.asif.meternotifier.repository.MeterAccountDetailsRepository;
import com.asif.meternotifier.service.MeterAccountDetailsService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Transactional
public class MeterAccountDetailsServiceImpl implements MeterAccountDetailsService {
    private final MeterAccountDetailsRepository meterAccountDetailsRepository;

    public MeterAccountDetailsServiceImpl(MeterAccountDetailsRepository meterAccountDetailsRepository) {
        this.meterAccountDetailsRepository = meterAccountDetailsRepository;
    }

    @Override
    public void save(MeterAccountDetails meterAccountDetails) {
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
