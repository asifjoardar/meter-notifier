package com.asif.meternotifier.validation;

import com.asif.meternotifier.repository.CustomerRepository;
import com.asif.meternotifier.repository.MeterAccountDetailsRepository;
import com.asif.meternotifier.service.CustomerService;
import org.springframework.stereotype.Service;

@Service
public class Validation {
    private final MeterAccountDetailsRepository meterAccountDetailsRepository;
    private final CustomerRepository customerRepository;
    private final CustomerService customerService;

    public Validation(MeterAccountDetailsRepository meterAccountDetailsRepository,
                      CustomerRepository customerRepository,
                      CustomerService customerService) {
        this.meterAccountDetailsRepository = meterAccountDetailsRepository;
        this.customerRepository = customerRepository;
        this.customerService = customerService;
    }

    public boolean accountMeterExist(String accountNo, String meterNo) {
        return meterAccountDetailsRepository.findByAccountNumber(accountNo).isPresent() ||
                meterAccountDetailsRepository.findByAccountNumber(meterNo).isPresent();
    }

    public boolean emailExist(String email) {
        return customerRepository.findByEmail(email).isPresent();
    }

    public boolean emailEnabled(Long id) {
        return customerService.findCustomerById(id).isEnabled();
    }
}
