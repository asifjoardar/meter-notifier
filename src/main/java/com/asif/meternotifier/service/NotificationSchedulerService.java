package com.asif.meternotifier.service;

import com.asif.meternotifier.dto.Data;
import com.asif.meternotifier.entity.MeterAccountDetails;
import com.asif.meternotifier.repository.MeterAccountDetailsRepository;
import com.asif.meternotifier.util.DataMapper;
import com.asif.meternotifier.util.EmailSender;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationSchedulerService {
    private final MeterAccountDetailsRepository meterAccountDetailsRepository;
    private final EmailSender emailSender;
    private final DataMapper dataMapper;

    public NotificationSchedulerService(MeterAccountDetailsRepository meterAccountDetailsRepository,
                                        EmailSender emailSender,
                                        DataMapper dataMapper){
        this.meterAccountDetailsRepository = meterAccountDetailsRepository;
        this.emailSender = emailSender;
        this.dataMapper = dataMapper;
    }
    Logger logger = LoggerFactory.getLogger(MeterAccountDetails.class);
    @Scheduled(fixedRate = 10000)
    /*@Scheduled(cron = "0 0 12 * * ?") // every day at 12pm(noon)*/
    public void day() throws JsonProcessingException {
        List<MeterAccountDetails> meterAccountDetailsList = meterAccountDetailsRepository.findAllByNotification_Status(true);
        for (MeterAccountDetails meterAccountDetails:meterAccountDetailsList){
            Data data = dataMapper.getDataFromMapper(meterAccountDetails.getAccountNumber(), meterAccountDetails.getMeterNumber());
            if(data.getBalance() <= meterAccountDetails.getNotification().getMinimumBalance() && !meterAccountDetails.getNotification().isNotified()){
                emailSender.send(meterAccountDetails.getNotification().getEmailToSendNotification(),
                        "Low Meter Balance Alert for Meter No: "+meterAccountDetails.getMeterNumber(),
                        "Dear Customer, your current balance is "+data.getBalance()+". Kindly recharge your account to avoid service disruptions.");
                meterAccountDetails.getNotification().setNotified(true);
                meterAccountDetailsRepository.save(meterAccountDetails);
            }
        }
    }
    @Scheduled(cron = "0 0 0 * * *") // every day at 12am(midnight)
    public void night() throws JsonProcessingException {
        List<MeterAccountDetails> meterAccountDetailsList = meterAccountDetailsRepository.findAllByNotification_Status(false);
        for (MeterAccountDetails meterAccountDetails:meterAccountDetailsList){
            Data data = dataMapper.getDataFromMapper(meterAccountDetails.getAccountNumber(), meterAccountDetails.getMeterNumber());
            if(data.getBalance() > meterAccountDetails.getNotification().getMinimumBalance() && meterAccountDetails.getNotification().isNotified()){
                meterAccountDetails.getNotification().setNotified(false);
                meterAccountDetails.setBalance(data.getBalance());
                meterAccountDetailsRepository.save(meterAccountDetails);
            }
        }
    }
}