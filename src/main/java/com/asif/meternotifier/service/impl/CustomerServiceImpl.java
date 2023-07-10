package com.asif.meternotifier.service.impl;

import com.asif.meternotifier.dto.CustomerDto;
import com.asif.meternotifier.entity.Customer;
import com.asif.meternotifier.repository.CustomerRepository;
import com.asif.meternotifier.service.CustomerService;

public class CustomerServiceImpl implements CustomerService {
    private CustomerRepository customerRepository;
    public CustomerServiceImpl(CustomerRepository customerRepository){
        this.customerRepository = customerRepository;
    }

    @Override
    public void saveCustomer(CustomerDto customerDto){

    }

    @Override
    public Customer findCustomerByEmail(String email){
        return customerRepository.findByEmail(email);
    }
}
