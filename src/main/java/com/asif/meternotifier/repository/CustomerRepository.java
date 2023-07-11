package com.asif.meternotifier.repository;


import com.asif.meternotifier.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    @Query(value = "select * from customer where email = ?1", nativeQuery = true)
    Customer findByEmail(String email);

    Optional<Customer> findById(Long id);
}
