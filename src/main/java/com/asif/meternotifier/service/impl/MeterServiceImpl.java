package com.asif.meternotifier.service.impl;

import com.asif.meternotifier.entity.Meter;
import com.asif.meternotifier.exception.BadRequestException;
import com.asif.meternotifier.exception.NotFoundException;
import com.asif.meternotifier.repository.MeterRepository;
import com.asif.meternotifier.service.MeterService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Transactional
public class MeterServiceImpl implements MeterService {
    private final MeterRepository meterAccountDetailsRepository;

    public MeterServiceImpl(MeterRepository meterAccountDetailsRepository) {
        this.meterAccountDetailsRepository = meterAccountDetailsRepository;
    }

    @Override
    public void save(Meter meterAccountDetails) {
        if (meterAccountDetailsRepository.findByAccountNumber(meterAccountDetails.getAccountNumber()).isPresent()) {
            throw new BadRequestException("Entered account / meter no already in use");
        }
        meterAccountDetailsRepository.save(meterAccountDetails);
    }

    @Override
    public Meter findByAccountNumber(String accountNumber) {
        Optional<Meter> meterAccountDetails = meterAccountDetailsRepository.findByAccountNumber(accountNumber);
        meterAccountDetails.orElseThrow(() -> new NotFoundException("Account no: " + accountNumber + " is not found"));
        return meterAccountDetails.get();
    }

    @Override
    public void deleteByAccountNumber(String accountNumber) {
        Optional<Meter> meterAccountDetails = meterAccountDetailsRepository.findByAccountNumber(accountNumber);
        meterAccountDetails.orElseThrow(() -> new NotFoundException("Account no: " + accountNumber + " is not found"));
        meterAccountDetailsRepository.deleteByAccountNumber(accountNumber);
    }
}
