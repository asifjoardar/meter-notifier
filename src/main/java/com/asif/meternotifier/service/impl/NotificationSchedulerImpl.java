package com.asif.meternotifier.service.impl;

import com.asif.meternotifier.entity.Customer;
import com.asif.meternotifier.entity.MeterAccountDetails;
import com.asif.meternotifier.repository.CustomerRepository;
import com.asif.meternotifier.service.NotificationScheduler;
import com.asif.meternotifier.util.EmailSender;
import com.asif.meternotifier.util.RequestSender;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

@Service
public class NotificationSchedulerImpl implements NotificationScheduler {
    private CustomerRepository customerRepository;
    private RequestSender requestSender;

    private EmailSender emailSender;


    public NotificationSchedulerImpl(CustomerRepository customerRepository, RequestSender requestSender, EmailSender emailSender){
        this.customerRepository = customerRepository;
        this.requestSender = requestSender;
        this.emailSender = emailSender;
    }
    //@Scheduled(fixedRate = 3600000)
    //@Scheduled(cron = "0/15 * * * * *")
    public void execute() throws JsonProcessingException {
        /*List<Customer> customers = customerRepository.findByNotified(false);
        for (Customer customer:customers){
            System.out.println("from scheduler");
            System.out.println(customers);
            List<MeterAccountDetails> meterAccountDetailsList = customer.getMeterAccountDetailsList();
            for (MeterAccountDetails meterAccountDetails:meterAccountDetailsList){
                //String url = "http://prepaid.desco.org.bd/api/tkdes/customer/getBalance?accountNo="+meterAccountDetails.getAccountNumber()+"&meterNo="+meterAccountDetails.getMeterNumber();
                //Map<String, Object> response = requestSender.request(url);
                if(*//*(int)response.get("balance")*//*200 <= 200 && customer.isNotified() == false){
                    emailSender.send(customer.getEmail(), "Yor balance is low", "Dear Customer your current balance is "+200+"please recharge.");
                    customer.setNotified(true);
                    customerRepository.save(customer);
                }
            }
        }*/
    }
}
