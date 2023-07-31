package com.asif.meternotifier.service;

import com.asif.meternotifier.dto.ApiData;
import com.asif.meternotifier.entity.Meter;
import com.asif.meternotifier.entity.Notification;
import com.asif.meternotifier.repository.MeterRepository;
import com.asif.meternotifier.util.DataMapperUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationSchedulerService {
    private final MeterRepository meterAccountDetailsRepository;
    private final EmailService emailService;
    private final DataMapperUtil dataMapperUtil;

    public NotificationSchedulerService(MeterRepository meterAccountDetailsRepository,
                                        EmailService emailService,
                                        DataMapperUtil dataMapperUtil) {
        this.meterAccountDetailsRepository = meterAccountDetailsRepository;
        this.emailService = emailService;
        this.dataMapperUtil = dataMapperUtil;
    }

    Logger logger = LoggerFactory.getLogger(Meter.class);

    //@Scheduled(fixedRate = 10000)
    @Scheduled(cron = "0 0 12 * * ?") // every day at 12pm(noon)
    public void day() throws JsonProcessingException {
        List<Meter> meterAccountDetailsList = meterAccountDetailsRepository.findAllByNotificationStatus(true);
        for (Meter meterAccountDetails : meterAccountDetailsList) {
            final String acNo = meterAccountDetails.getAccountNumber();
            final String meterNo = meterAccountDetails.getMeterNumber();
            Notification notification = meterAccountDetails.getNotification();
            ApiData apiData = dataMapperUtil.getCustomerDataFromApi(acNo, meterNo);
            if (apiData.getBalance() <= notification.getMinimumBalance() && !notification.isNotified()) {
                emailService.sendEmail(notification.getEmailToSendNotification(),
                        "Low Meter Balance Alert for Meter No: " + meterNo,
                        "Dear Customer, your current balance is "
                                + apiData.getBalance()
                                + ". Kindly recharge your account to avoid service disruptions.");
                notification.setNotified(true);
                meterAccountDetailsRepository.save(meterAccountDetails);
            }
        }
    }

    @Scheduled(cron = "0 0 0 * * *") // every day at 12am(midnight)
    public void night() throws JsonProcessingException {
        List<Meter> meterAccountDetailsList = meterAccountDetailsRepository.findAllByNotificationStatus(false);
        for (Meter meterAccountDetails : meterAccountDetailsList) {
            final String acNo = meterAccountDetails.getAccountNumber();
            final String meterNo = meterAccountDetails.getMeterNumber();
            Notification notification = meterAccountDetails.getNotification();
            ApiData apiData = dataMapperUtil.getCustomerDataFromApi(acNo, meterNo);
            if (apiData.getBalance() > notification.getMinimumBalance() && notification.isNotified()) {
                notification.setNotified(false);
                meterAccountDetails.setBalance(apiData.getBalance());
                meterAccountDetailsRepository.save(meterAccountDetails);
            }
        }
    }
}