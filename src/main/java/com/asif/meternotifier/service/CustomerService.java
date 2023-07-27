package com.asif.meternotifier.service;

import com.asif.meternotifier.entity.Customer;
import com.asif.meternotifier.entity.MeterAccountDetails;

public interface CustomerService {
    void save(Customer customer);

    Customer findCustomerByEmail(String email);

    Customer findCustomerById(Long id);

    boolean confirmEmail(String confirmationToken);
}
