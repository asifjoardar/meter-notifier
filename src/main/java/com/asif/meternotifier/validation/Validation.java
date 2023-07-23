package com.asif.meternotifier.validation;

import com.asif.meternotifier.repository.CustomerRepository;
import com.asif.meternotifier.repository.MeterAccountDetailsRepository;
import org.springframework.stereotype.Service;

@Service
public class Validation {
    private final MeterAccountDetailsRepository meterAccountDetailsRepository;
    private final CustomerRepository customerRepository;

    public Validation(MeterAccountDetailsRepository meterAccountDetailsRepository,
                      CustomerRepository customerRepository) {
        this.meterAccountDetailsRepository = meterAccountDetailsRepository;
        this.customerRepository = customerRepository;
    }

    public boolean accountMeterExist(String accountNo, String meterNo) {
        return meterAccountDetailsRepository.findByAccountNumber(accountNo).isEmpty() &&
                meterAccountDetailsRepository.findByAccountNumber(meterNo).isEmpty();
    }

    public boolean emailExist(String email) {
        return customerRepository.findByEmail(email) != null;
    }

    public boolean emailEnabled(Long id) {
        return customerRepository.findById(id).get().isEnabled();
    }
}
