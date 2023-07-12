package com.asif.meternotifier.util;

import com.asif.meternotifier.dto.Data;
import com.asif.meternotifier.entity.MeterAccountDetails;
import com.asif.meternotifier.repository.MeterAccountDetailsRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class NotificationScheduler {
    private MeterAccountDetailsRepository meterAccountDetailsRepository;
    private RequestSender requestSender;
    private EmailSender emailSender;


    public NotificationScheduler(MeterAccountDetailsRepository meterAccountDetailsRepository, RequestSender requestSender, EmailSender emailSender){
        this.meterAccountDetailsRepository = meterAccountDetailsRepository;
        this.requestSender = requestSender;
        this.emailSender = emailSender;
    }
    Logger logger = LoggerFactory.getLogger(MeterAccountDetails.class);
    @Scheduled(fixedRate = 5000)
    public void execute() throws JsonProcessingException {
        System.out.println("scheduler call in " + new Date().toString());

        List<MeterAccountDetails> meterAccountDetailsList = meterAccountDetailsRepository.findAllByNotification(true);
        for (MeterAccountDetails meterAccountDetails:meterAccountDetailsList){
            String url = "http://prepaid.desco.org.bd/api/tkdes/customer/getBalance?accountNo="+
                            meterAccountDetails.getAccountNumber()+
                            "&meterNo="+
                            meterAccountDetails.getMeterNumber();
            ObjectMapper mapper = new ObjectMapper();
            Data data = mapper.treeToValue(requestSender.request(url), Data.class);
            if(data.getBalance() <= 1500.00 && meterAccountDetails.isNotified() == false){
                emailSender.send(meterAccountDetails.getCustomer().getEmail(), "Yor balance is low", "Dear Customer your current balance is "+200+"please recharge.");
                meterAccountDetails.setNotified(true);
                meterAccountDetailsRepository.save(meterAccountDetails);
            }
            logger.info("logging date: " + meterAccountDetails);
        }
    }
}