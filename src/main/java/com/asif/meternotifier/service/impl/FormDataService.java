package com.asif.meternotifier.service.impl;

import com.asif.meternotifier.dto.FormData;
import com.asif.meternotifier.entity.Customer;
import com.asif.meternotifier.entity.Meter;
import com.asif.meternotifier.entity.Notification;
import com.asif.meternotifier.service.CustomerService;
import com.asif.meternotifier.service.MeterService;
import com.asif.meternotifier.service.NotificationService;
import org.springframework.stereotype.Service;

@Service
public class FormDataService {
    private final CustomerService customerService;
    private final MeterService meterAccountDetailsService;
    private final NotificationService notificationService;

    public FormDataService(CustomerService customerService,
                           MeterService meterAccountDetailsService,
                           NotificationService notificationService) {
        this.customerService = customerService;
        this.notificationService = notificationService;
        this.meterAccountDetailsService = meterAccountDetailsService;
    }

    public Customer saveFormDataToTables(FormData formData) {

        Notification notification = new Notification();
        Meter meterAccountDetails = new Meter();
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
        Meter meterAccountDetails = meterAccountDetailsService.findByAccountNumber(formData.getAccountNumber());
        Notification notification = meterAccountDetails.getNotification();
        notification.setEmailToSendNotification(formData.getSendNotificationTo());
        notification.setMinimumBalance(formData.getMinBalance());
        notification.setStatus(formData.isNotificationStatus());
        notificationService.save(notification);
    }
}
