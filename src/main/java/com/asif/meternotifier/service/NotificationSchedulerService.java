package com.asif.meternotifier.service;

import com.asif.meternotifier.dto.Data;
import com.asif.meternotifier.entity.MeterAccountDetails;
import com.asif.meternotifier.entity.Notification;
import com.asif.meternotifier.repository.MeterAccountDetailsRepository;
import com.asif.meternotifier.util.DataMapperUtil;
import com.asif.meternotifier.util.EmailSenderUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationSchedulerService {
    private final MeterAccountDetailsRepository meterAccountDetailsRepository;
    private final EmailSenderUtil emailSenderUtil;
    private final DataMapperUtil dataMapperUtil;

    public NotificationSchedulerService(MeterAccountDetailsRepository meterAccountDetailsRepository,
                                        EmailSenderUtil emailSenderUtil,
                                        DataMapperUtil dataMapperUtil) {
        this.meterAccountDetailsRepository = meterAccountDetailsRepository;
        this.emailSenderUtil = emailSenderUtil;
        this.dataMapperUtil = dataMapperUtil;
    }

    Logger logger = LoggerFactory.getLogger(MeterAccountDetails.class);

    //@Scheduled(fixedRate = 10000)
    @Scheduled(cron = "0 0 12 * * ?") // every day at 12pm(noon)
    public void day() throws JsonProcessingException {
        List<MeterAccountDetails> meterAccountDetailsList = meterAccountDetailsRepository.findAllByNotification_Status(true);
        for (MeterAccountDetails meterAccountDetails : meterAccountDetailsList) {
            final String acNo = meterAccountDetails.getAccountNumber();
            final String meterNo = meterAccountDetails.getMeterNumber();
            Notification notification = meterAccountDetails.getNotification();
            Data data = dataMapperUtil.getDataFromMapper(acNo, meterNo);
            if (data.getBalance() <= notification.getMinimumBalance() && !notification.isNotified()) {
                emailSenderUtil.send(notification.getEmailToSendNotification(),
                        "Low Meter Balance Alert for Meter No: " + meterNo,
                        "Dear Customer, your current balance is "
                                + data.getBalance()
                                + ". Kindly recharge your account to avoid service disruptions.");
                notification.setNotified(true);
                meterAccountDetailsRepository.save(meterAccountDetails);
            }
        }
    }

    @Scheduled(cron = "0 0 0 * * *") // every day at 12am(midnight)
    public void night() throws JsonProcessingException {
        List<MeterAccountDetails> meterAccountDetailsList = meterAccountDetailsRepository.findAllByNotification_Status(false);
        for (MeterAccountDetails meterAccountDetails : meterAccountDetailsList) {
            final String acNo = meterAccountDetails.getAccountNumber();
            final String meterNo = meterAccountDetails.getMeterNumber();
            Notification notification = meterAccountDetails.getNotification();
            Data data = dataMapperUtil.getDataFromMapper(acNo, meterNo);
            if (data.getBalance() > notification.getMinimumBalance() && notification.isNotified()) {
                notification.setNotified(false);
                meterAccountDetails.setBalance(data.getBalance());
                meterAccountDetailsRepository.save(meterAccountDetails);
            }
        }
    }
}