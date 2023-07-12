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
    @Scheduled(fixedRate = 2000)
    public void execute() throws JsonProcessingException {
        List<MeterAccountDetails> meterAccountDetailsListTrue = meterAccountDetailsRepository.findAllByNotification(true);
        List<MeterAccountDetails> meterAccountDetailsListFalse = meterAccountDetailsRepository.findAllByNotification(false);
        for (MeterAccountDetails meterAccountDetails:meterAccountDetailsListFalse){
            String url = urlMaker(meterAccountDetails.getAccountNumber(), meterAccountDetails.getMeterNumber());
            Data data = getDataFromMapper(url);
            if(data.getBalance() > 1500.00 && meterAccountDetails.isNotified() == true){
                meterAccountDetails.setNotified(false);
                meterAccountDetailsRepository.save(meterAccountDetails);
            }
        }
        for (MeterAccountDetails meterAccountDetails:meterAccountDetailsListTrue){
            String url = urlMaker(meterAccountDetails.getAccountNumber(), meterAccountDetails.getMeterNumber());
            Data data = getDataFromMapper(url);
            if(data.getBalance() <= 1500.00 && meterAccountDetails.isNotified() == false){
                emailSender.send(meterAccountDetails.getCustomer().getEmail(), "Balance is low", "Dear Customer your current balance is "+data.getBalance()+" taka please recharge.");
                meterAccountDetails.setNotified(true);
                meterAccountDetailsRepository.save(meterAccountDetails);
            }
            logger.info("logging date: " + meterAccountDetails);
        }
    }
    public String urlMaker(String acNo, String meterNo){
        return "http://prepaid.desco.org.bd/api/tkdes/customer/getBalance?accountNo="+ acNo+ "&meterNo="+ meterNo;
    }
    public Data getDataFromMapper(String url) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.treeToValue(this.requestSender.request(url), Data.class);
    }
}