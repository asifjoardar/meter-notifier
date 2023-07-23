package com.asif.meternotifier.service;

import com.asif.meternotifier.entity.Customer;
import com.asif.meternotifier.entity.MeterAccountDetails;

public interface CustomerService {
    void saveCustomer(Customer customer, MeterAccountDetails meterAccountDetails);

    void updateCustomer(MeterAccountDetails meterAccountDetails);

    Customer findCustomerByEmail(String email);

    Customer findCustomerById(Long id);

    boolean confirmEmail(String confirmationToken);
}
