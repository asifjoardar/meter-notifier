package com.asif.meternotifier.repository;

import com.asif.meternotifier.entity.MeterAccountDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MeterAccountDetailsRepository extends JpaRepository<MeterAccountDetails, String> {
    Optional<MeterAccountDetails> findByAccountNumber(String accountNumber);
    List<MeterAccountDetails> findAllByNotification_Status(boolean status);
    void deleteByAccountNumber(String accountNumber);
}
