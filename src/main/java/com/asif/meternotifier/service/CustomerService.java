package com.asif.meternotifier.service;

import com.asif.meternotifier.dto.CustomerDto;
import com.asif.meternotifier.entity.Customer;

import java.util.Optional;

public interface CustomerService {
    void saveCustomer(CustomerDto customerDto);
    Customer findCustomerByEmail(String email);
    Optional<Customer> findCustomerById(Long id);
    boolean confirmEmail(String confirmationToken);
}
