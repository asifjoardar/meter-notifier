package com.asif.meternotifier.validation;

import com.asif.meternotifier.service.CustomerService;
import org.springframework.stereotype.Service;

@Service
public class Validation {
    private final CustomerService customerService;

    public Validation(CustomerService customerService) {
        this.customerService = customerService;
    }

    public boolean emailEnabled(Long id) {
        return customerService.findCustomerById(id).isEnabled();
    }
}
