package com.asif.meternotifier.repository;

import com.asif.meternotifier.entity.MeterAccountDetails;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MeterAccountDetailsRepository extends JpaRepository<MeterAccountDetails, String> {
    MeterAccountDetails findByAccountNumber(String accountNumber);
    MeterAccountDetails findByMeterNumber(String meterNumber);
}
