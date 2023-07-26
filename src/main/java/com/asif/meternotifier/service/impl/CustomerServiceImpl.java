package com.asif.meternotifier.service.impl;

import com.asif.meternotifier.entity.ConfirmationToken;
import com.asif.meternotifier.entity.Customer;
import com.asif.meternotifier.entity.MeterAccountDetails;
import com.asif.meternotifier.entity.Notification;
import com.asif.meternotifier.exception.CustomerNotFoundException;
import com.asif.meternotifier.repository.ConfirmationTokenRepository;
import com.asif.meternotifier.repository.CustomerRepository;
import com.asif.meternotifier.repository.NotificationRepository;
import com.asif.meternotifier.service.CustomerService;
import com.asif.meternotifier.service.MeterAccountDetailsService;
import com.asif.meternotifier.util.EmailSenderUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Service
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;
    private final ConfirmationTokenRepository confirmationTokenRepository;
    private final EmailSenderUtil emailSenderUtil;
    private final NotificationRepository notificationRepository;
    private final MeterAccountDetailsService meterAccountDetailsService;

    @Value("${host}")
    private String host;

    public CustomerServiceImpl(CustomerRepository customerRepository,
                               ConfirmationTokenRepository confirmationTokenRepository,
                               EmailSenderUtil emailSenderUtil,
                               NotificationRepository notificationRepository,
                               MeterAccountDetailsService meterAccountDetailsService) {
        this.customerRepository = customerRepository;
        this.confirmationTokenRepository = confirmationTokenRepository;
        this.emailSenderUtil = emailSenderUtil;
        this.notificationRepository = notificationRepository;
        this.meterAccountDetailsService = meterAccountDetailsService;
    }

    @Override
    public void saveCustomer(Customer customer, MeterAccountDetails meterAccountDetails) {
        Notification notification = new Notification();

        if (customerRepository.findByEmail(customer.getEmail()).isEmpty()) {
            customerRepository.save(customer);
            generateAndSendToken(customer);
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

    private void generateAndSendToken(Customer customer) {
        // token generate
        ConfirmationToken confirmationToken = new ConfirmationToken();
        confirmationToken.setCustomer(customer);
        confirmationToken.setConfirmationToken(UUID.randomUUID().toString());
        confirmationToken.setCreatedDate(new Date());
        confirmationTokenRepository.save(confirmationToken);
        // email sender
        emailSenderUtil.send(customer.getEmail(),
                "Validate Your Email Address - Action Required",
                "To confirm your account, please click here : "
                        + host
                        + "/confirm-account?token="
                        + confirmationToken.getConfirmationToken());
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
        ConfirmationToken token = confirmationTokenRepository.findByConfirmationToken(confirmationToken);
        if (token != null) {
            final String email = token.getCustomer().getEmail();
            Optional<Customer> customer = customerRepository.findByEmail(email);
            customer.orElseThrow(() -> new CustomerNotFoundException("Customer with email " + email + " not found"));
            customer.get().setEnabled(true);
            customerRepository.save(customer.get());
            return true;
        }
        return false;
    }
}
