package com.asif.meternotifier.service.impl;

import com.asif.meternotifier.entity.ConfirmationToken;
import com.asif.meternotifier.entity.Customer;
import com.asif.meternotifier.exception.CustomerNotFoundException;
import com.asif.meternotifier.repository.CustomerRepository;
import com.asif.meternotifier.service.ConfirmationTokenService;
import com.asif.meternotifier.service.CustomerService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;
    private final ConfirmationTokenService confirmationTokenService;

    public CustomerServiceImpl(CustomerRepository customerRepository,
                               ConfirmationTokenService confirmationTokenService) {
        this.customerRepository = customerRepository;
        this.confirmationTokenService = confirmationTokenService;
    }

    @Override
    public void save(Customer customer) {
        customerRepository.save(customer);
    }

    @Override
    public Customer findCustomerByEmail(String email) {
        Optional<Customer> customer = customerRepository.findByEmail(email);
        customer.orElseThrow(() -> new CustomerNotFoundException("Customer with email " + email + " not found"));
        return customer.get();
    }

    @Override
    public Customer findCustomerById(Long id) {
        Optional<Customer> customer = customerRepository.findById(id);
        customer.orElseThrow(() -> new CustomerNotFoundException("Customer with id " + id + " not found"));
        return customer.get();
    }

    @Override
    public boolean confirmEmail(String confirmationToken) {
        ConfirmationToken token = confirmationTokenService.findByConfirmationToken(confirmationToken);
        if (token != null) {
            final String email = token.getCustomer().getEmail();
            Customer customer = findCustomerByEmail(email);
            customer.setEnabled(true);
            customerRepository.save(customer);
            return true;
        }
        return false;
    }
}
