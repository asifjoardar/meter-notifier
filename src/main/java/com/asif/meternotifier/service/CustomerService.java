package com.asif.meternotifier.service;

import com.asif.meternotifier.dto.FormDataDto;
import com.asif.meternotifier.entity.Customer;
import com.asif.meternotifier.entity.Meter;

public interface CustomerService {
    Customer save(FormDataDto formData);

    void addNewMeter(Customer customer, Meter meter);

    Customer findCustomerByEmail(String email);

    Customer findCustomerById(Long id);

    void confirmEmail(String confirmationToken);
}
