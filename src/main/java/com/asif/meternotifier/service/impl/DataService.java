package com.asif.meternotifier.service.impl;

import com.asif.meternotifier.dto.SignupFormData;
import com.asif.meternotifier.entity.Customer;
import com.asif.meternotifier.entity.MeterAccountDetails;
import com.asif.meternotifier.entity.Notification;
import com.asif.meternotifier.service.ConfirmationTokenService;
import com.asif.meternotifier.service.CustomerService;
import com.asif.meternotifier.service.MeterAccountDetailsService;
import com.asif.meternotifier.service.NotificationService;
import org.springframework.stereotype.Service;

@Service
public class DataService {
    private final CustomerService customerService;
    private final MeterAccountDetailsService meterAccountDetailsService;
    private final NotificationService notificationService;
    private final ConfirmationTokenService confirmationTokenService;

    public DataService(CustomerService customerService,
                       MeterAccountDetailsService meterAccountDetailsService,
                       NotificationService notificationService,
                       ConfirmationTokenService confirmationTokenService){
        this.customerService = customerService;
        this.notificationService = notificationService;
        this.meterAccountDetailsService = meterAccountDetailsService;
        this.confirmationTokenService = confirmationTokenService;
    }
    public Customer saveFormDataToTables(SignupFormData signupFormData){
        Notification notification = new Notification();
        notification.setEmailToSendNotification(signupFormData.getEmail());
        notificationService.save(notification);

        MeterAccountDetails meterAccountDetails = new MeterAccountDetails();
        meterAccountDetails.setAccountNumber(signupFormData.getAccountNumber());
        meterAccountDetails.setMeterNumber(signupFormData.getMeterNumber());
        meterAccountDetails.setBalance(signupFormData.getBalance());
        meterAccountDetails.setNotification(notification);
        meterAccountDetailsService.save(meterAccountDetails);

        Customer customer = new Customer();
        customer.setEmail(signupFormData.getEmail());
        customer.getMeterAccountDetailsList().add(meterAccountDetails);
        customerService.save(customer);

        meterAccountDetails.setCustomer(customer);
        meterAccountDetailsService.save(meterAccountDetails);

        confirmationTokenService.generateAndSendToken(customer);
        return customer;
    }
}
