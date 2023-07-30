package com.asif.meternotifier.service.impl;

import com.asif.meternotifier.dto.FormData;
import com.asif.meternotifier.entity.Customer;
import com.asif.meternotifier.entity.MeterAccountDetails;
import com.asif.meternotifier.entity.Notification;
import com.asif.meternotifier.service.CustomerService;
import com.asif.meternotifier.service.MeterAccountDetailsService;
import com.asif.meternotifier.service.NotificationService;
import org.springframework.stereotype.Service;

@Service
public class DataService {
    private final CustomerService customerService;
    private final MeterAccountDetailsService meterAccountDetailsService;
    private final NotificationService notificationService;

    public DataService(CustomerService customerService,
                       MeterAccountDetailsService meterAccountDetailsService,
                       NotificationService notificationService) {
        this.customerService = customerService;
        this.notificationService = notificationService;
        this.meterAccountDetailsService = meterAccountDetailsService;
    }

    public Customer saveFormDataToTables(FormData formData) {

        Notification notification = new Notification();
        MeterAccountDetails meterAccountDetails = new MeterAccountDetails();
        Customer customer = new Customer();
        customer.setEmail(formData.getEmail());
        customerService.save(customer);

        notification.setEmailToSendNotification(formData.getEmail());
        notificationService.save(notification);

        meterAccountDetails.setAccountNumber(formData.getAccountNumber());
        meterAccountDetails.setMeterNumber(formData.getMeterNumber());
        meterAccountDetails.setBalance(formData.getBalance());
        meterAccountDetails.setNotification(notification);
        meterAccountDetails.setCustomer(customer);
        meterAccountDetailsService.save(meterAccountDetails);

        customer.getMeterAccountDetailsList().add(meterAccountDetails);
        customerService.save(customer);

        return customer;
    }

    public void updateFormDataToTables(FormData formData) {

        Notification notification = notificationService.findByEmail(formData.getEmail());
        notification.setEmailToSendNotification(formData.getSendNotificationTo());
        notification.setMinimumBalance(formData.getMinBalance());
        notification.setStatus(formData.isNotificationStatus());
        notificationService.save(notification);

        MeterAccountDetails meterAccountDetails = meterAccountDetailsService.findByAccountNumber(formData.getAccountNumber());
        meterAccountDetails.setNotification(notification);
        meterAccountDetailsService.save(meterAccountDetails);
    }
}
