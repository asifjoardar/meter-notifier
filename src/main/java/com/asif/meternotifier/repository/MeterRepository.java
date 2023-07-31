package com.asif.meternotifier.repository;

import com.asif.meternotifier.entity.Meter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MeterRepository extends JpaRepository<Meter, String> {
    Optional<Meter> findByAccountNumber(String accountNumber);
    List<Meter> findAllByNotificationStatus(boolean status);
    void deleteByAccountNumber(String accountNumber);
}
