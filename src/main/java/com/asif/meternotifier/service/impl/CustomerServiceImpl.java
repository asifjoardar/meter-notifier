package com.asif.meternotifier.service.impl;

import com.asif.meternotifier.dto.FormData;
import com.asif.meternotifier.entity.ConfirmationToken;
import com.asif.meternotifier.entity.Customer;
import com.asif.meternotifier.entity.Meter;
import com.asif.meternotifier.entity.Notification;
import com.asif.meternotifier.exception.CustomerNotFoundException;
import com.asif.meternotifier.repository.CustomerRepository;
import com.asif.meternotifier.service.ConfirmationTokenService;
import com.asif.meternotifier.service.CustomerService;
import com.asif.meternotifier.service.MeterService;
import com.asif.meternotifier.service.NotificationService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;
    private final ConfirmationTokenService confirmationTokenService;
    private final MeterService meterService;
    private final NotificationService notificationService;

    public CustomerServiceImpl(CustomerRepository customerRepository,
                               ConfirmationTokenService confirmationTokenService,
                               MeterService meterAccountDetailsService,
                               NotificationService notificationService) {
        this.customerRepository = customerRepository;
        this.confirmationTokenService = confirmationTokenService;
        this.notificationService = notificationService;
        this.meterService = meterAccountDetailsService;
    }

    @Override
    public Customer save(FormData formData) {

        Notification notification = new Notification();
        Meter meterAccountDetails = new Meter();
        Customer customer = new Customer();
        customer.setEmail(formData.getEmail());
        customerRepository.save(customer);

        notification.setEmailToSendNotification(formData.getEmail());
        notificationService.save(notification);

        meterAccountDetails.setAccountNumber(formData.getAccountNumber());
        meterAccountDetails.setMeterNumber(formData.getMeterNumber());
        meterAccountDetails.setBalance(formData.getBalance());
        meterAccountDetails.setNotification(notification);
        meterAccountDetails.setCustomer(customer);
        meterService.save(meterAccountDetails);

        customer.getMeterAccountDetailsList().add(meterAccountDetails);
        customerRepository.save(customer);

        return customer;
    }

    @Override
    public void addNewMeter(Customer customer, Meter meter){
        Notification notification = new Notification();
        notification.setEmailToSendNotification(customer.getEmail());
        notificationService.save(notification);
        meter.setNotification(notification);
        meter.setCustomer(customer);
        meterService.save(meter);
        customer.getMeterAccountDetailsList().add(meter);
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
