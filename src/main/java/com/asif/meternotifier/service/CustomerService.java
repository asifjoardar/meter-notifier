package com.asif.meternotifier.service;

import com.asif.meternotifier.entity.Customer;
import com.asif.meternotifier.entity.MeterAccountDetails;

import java.util.Optional;

public interface CustomerService {
    void saveCustomer(Customer customer, MeterAccountDetails meterAccountDetails);
    Customer findCustomerByEmail(String email);
    Optional<Customer> findCustomerById(Long id);
    boolean confirmEmail(String confirmationToken);
}
