package com.asif.meternotifier.service.impl;

import com.asif.meternotifier.entity.ConfirmationToken;
import com.asif.meternotifier.entity.Customer;
import com.asif.meternotifier.entity.MeterAccountDetails;
import com.asif.meternotifier.entity.Notification;
import com.asif.meternotifier.exception.CustomerNotFoundException;
import com.asif.meternotifier.repository.CustomerRepository;
import com.asif.meternotifier.repository.NotificationRepository;
import com.asif.meternotifier.service.ConfirmationTokenService;
import com.asif.meternotifier.service.CustomerService;
import com.asif.meternotifier.service.MeterAccountDetailsService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;
    private final NotificationRepository notificationRepository;
    private final MeterAccountDetailsService meterAccountDetailsService;
    private final ConfirmationTokenService confirmationTokenService;

    @Value("${host}")
    private String host;

    public CustomerServiceImpl(CustomerRepository customerRepository,
                               NotificationRepository notificationRepository,
                               MeterAccountDetailsService meterAccountDetailsService,
                               ConfirmationTokenService confirmationTokenService) {
        this.customerRepository = customerRepository;
        this.notificationRepository = notificationRepository;
        this.meterAccountDetailsService = meterAccountDetailsService;
        this.confirmationTokenService = confirmationTokenService;
    }

    @Override
    public void saveCustomer(Customer customer, MeterAccountDetails meterAccountDetails) {
        Notification notification = new Notification();

        if (customerRepository.findByEmail(customer.getEmail()).isEmpty()) {
            customerRepository.save(customer);
            confirmationTokenService.generateAndSendToken(customer);
        } else {
            notification.setId(meterAccountDetails.getNotification().getId());
        }

        notification.setEmailToSendNotification(customer.getEmail());
        notificationRepository.save(notification);

        meterAccountDetailsService.saveMeterAccountDetails(meterAccountDetails);
        customer.getMeterAccountDetailsList().add(meterAccountDetails);
        customerRepository.save(customer);

        meterAccountDetails.setCustomer(customer);
        meterAccountDetails.setNotification(notification);
        meterAccountDetailsService.saveMeterAccountDetails(meterAccountDetails);
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
