package com.asif.meternotifier.service;

import com.asif.meternotifier.dto.FormData;
import com.asif.meternotifier.entity.Customer;
import com.asif.meternotifier.entity.Meter;

public interface CustomerService {
    Customer save(FormData formData);

    void addNewMeter(Customer customer, Meter meter);

    Customer findCustomerByEmail(String email);

    Customer findCustomerById(Long id);

    boolean confirmEmail(String confirmationToken);
}
