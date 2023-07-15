package com.asif.meternotifier.service;

import com.asif.meternotifier.dto.Data;
import com.asif.meternotifier.entity.MeterAccountDetails;
import com.asif.meternotifier.repository.MeterAccountDetailsRepository;
import com.asif.meternotifier.util.DataMapper;
import com.asif.meternotifier.util.EmailSender;
import com.asif.meternotifier.util.RequestSender;
import com.asif.meternotifier.util.UrlMaker;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationSchedulerService {
    private MeterAccountDetailsRepository meterAccountDetailsRepository;
    private RequestSender requestSender;
    private EmailSender emailSender;

    private DataMapper dataMapper;

    public NotificationSchedulerService(MeterAccountDetailsRepository meterAccountDetailsRepository,
                                        RequestSender requestSender,
                                        EmailSender emailSender,
                                        DataMapper dataMapper){
        this.meterAccountDetailsRepository = meterAccountDetailsRepository;
        this.requestSender = requestSender;
        this.emailSender = emailSender;
        this.dataMapper = dataMapper;
    }
    Logger logger = LoggerFactory.getLogger(MeterAccountDetails.class);
    @Scheduled(fixedRate = 2000)
    public void execute() throws JsonProcessingException {
        List<MeterAccountDetails> meterAccountDetailsListTrue = meterAccountDetailsRepository.findAllByNotification(true);
        List<MeterAccountDetails> meterAccountDetailsListFalse = meterAccountDetailsRepository.findAllByNotification(false);
        for (MeterAccountDetails meterAccountDetails:meterAccountDetailsListFalse){
            Data data = dataMapper.getDataFromMapper(meterAccountDetails.getAccountNumber(), meterAccountDetails.getMeterNumber());
            if(data.getBalance() > 1500.00 && meterAccountDetails.isNotified() == true){
                meterAccountDetails.setNotified(false);
                meterAccountDetailsRepository.save(meterAccountDetails);
            }
        }
        for (MeterAccountDetails meterAccountDetails:meterAccountDetailsListTrue){
            Data data = dataMapper.getDataFromMapper(meterAccountDetails.getAccountNumber(), meterAccountDetails.getMeterNumber());
            if(data.getBalance() <= 1500.00 && meterAccountDetails.isNotified() == false){
                emailSender.send(meterAccountDetails.getCustomer().getEmail(), "Balance is low", "Dear Customer your current balance is "+data.getBalance()+" taka please recharge.");
                meterAccountDetails.setNotified(true);
                meterAccountDetailsRepository.save(meterAccountDetails);
            }
            logger.info("logging date: " + meterAccountDetails);
        }
    }
}