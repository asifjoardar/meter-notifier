package com.asif.meternotifier.service;

import com.asif.meternotifier.dto.CustomerDto;
import com.asif.meternotifier.entity.Customer;

public interface CustomerService {
    void saveCustomer(CustomerDto customerDto);
    Customer findCustomerByEmail(String email);
}
