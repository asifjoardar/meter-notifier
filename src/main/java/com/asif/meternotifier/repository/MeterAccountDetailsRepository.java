package com.asif.meternotifier.repository;

import com.asif.meternotifier.entity.Customer;
import com.asif.meternotifier.entity.MeterAccountDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MeterAccountDetailsRepository extends JpaRepository<MeterAccountDetails, String> {
    MeterAccountDetails findByAccountNumber(String accountNumber);
    MeterAccountDetails findByMeterNumber(String meterNumber);
    List<MeterAccountDetails> findAllByNotified(boolean notification);
}