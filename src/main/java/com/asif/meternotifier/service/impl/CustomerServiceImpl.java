package com.asif.meternotifier.service.impl;

import com.asif.meternotifier.entity.ConfirmationToken;
import com.asif.meternotifier.entity.Customer;
import com.asif.meternotifier.entity.MeterAccountDetails;
import com.asif.meternotifier.entity.Notification;
import com.asif.meternotifier.repository.ConfirmationTokenRepository;
import com.asif.meternotifier.repository.CustomerRepository;
import com.asif.meternotifier.repository.MeterAccountDetailsRepository;
import com.asif.meternotifier.repository.NotificationRepository;
import com.asif.meternotifier.service.CustomerService;
import com.asif.meternotifier.util.EmailSender;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;
    private final MeterAccountDetailsRepository meterAccountDetailsRepository;
    private final ConfirmationTokenRepository confirmationTokenRepository;
    private final EmailSender emailSender;
    private final NotificationRepository notificationRepository;

    public CustomerServiceImpl(CustomerRepository customerRepository,
                               ConfirmationTokenRepository confirmationTokenRepository,
                               MeterAccountDetailsRepository meterAccountDetailsRepository,
                               EmailSender emailSender,
                               NotificationRepository notificationRepository){
        this.customerRepository = customerRepository;
        this.confirmationTokenRepository = confirmationTokenRepository;
        this.meterAccountDetailsRepository = meterAccountDetailsRepository;
        this.emailSender = emailSender;
        this.notificationRepository = notificationRepository;
    }

    @Override
    public void saveCustomer(Customer customer, MeterAccountDetails meterAccountDetails){
        if(customerRepository.findByEmail(customer.getEmail()) == null){
            customerRepository.save(customer);
            generateAndSendToken(customer);
        }

        Notification notification = new Notification();
        notification.setEmailToSendNotification(customer.getEmail());
        notificationRepository.save(notification);

        meterAccountDetailsRepository.save(meterAccountDetails);
        customer.getMeterAccountDetailsList().add(meterAccountDetails);
        customerRepository.save(customer);

        meterAccountDetails.setCustomer(customer);
        meterAccountDetails.setNotification(notification);
        meterAccountDetailsRepository.save(meterAccountDetails);
    }

    private void generateAndSendToken(Customer customer) {
        // token generate
        ConfirmationToken confirmationToken = new ConfirmationToken(customer);
        confirmationTokenRepository.save(confirmationToken);
        // email sender
        emailSender.send(customer.getEmail(), "Validate Your Email Address - Action Required", "To confirm your account, please click here : "
                +"https://meter-notifier-production.up.railway.app/confirm-account?token="+confirmationToken.getConfirmationToken());
    }

    @Override
    public void updateCustomer(MeterAccountDetails meterAccountDetails){
        Customer customer = customerRepository.findByEmail(meterAccountDetails.getCustomer().getEmail());
        Notification notification = meterAccountDetails.getNotification();
        meterAccountDetails = meterAccountDetailsRepository.findByAccountNumber(meterAccountDetails.getAccountNumber());
        notification.setId(meterAccountDetails.getNotification().getId());
        notificationRepository.save(notification);
        meterAccountDetails.setNotification(notification);
        meterAccountDetails.setCustomer(customer);
        meterAccountDetailsRepository.save(meterAccountDetails);
    }

    @Override
    public Customer findCustomerByEmail(String email){
        return customerRepository.findByEmail(email);
    }

    @Override
    public Optional<Customer> findCustomerById(Long id){
        return customerRepository.findById(id);
    }

    @Override
    public boolean confirmEmail(String confirmationToken) {
        ConfirmationToken token = confirmationTokenRepository.findByConfirmationToken(confirmationToken);
        if(token != null) {
            Customer customer = customerRepository.findByEmail(token.getCustomer().getEmail());
            customer.setEnabled(true);
            customerRepository.save(customer);
            return true;
        }
        return false;
    }
}
