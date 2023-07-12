package com.asif.meternotifier.controller;

import com.asif.meternotifier.entity.MeterAccountDetails;
import com.asif.meternotifier.repository.MeterAccountDetailsRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class NotificationController {
    private MeterAccountDetailsRepository meterAccountDetailsRepository;
    public NotificationController(MeterAccountDetailsRepository meterAccountDetailsRepository){
        this.meterAccountDetailsRepository = meterAccountDetailsRepository;
    }
    @GetMapping("/notification-starter/{accountNumber}")
    public String notification(@PathVariable("accountNumber") String accountNumber, Model model){
        MeterAccountDetails meterAccountDetails = meterAccountDetailsRepository.findByAccountNumber(accountNumber);
        model.addAttribute("accountDetails", meterAccountDetails);
        return "get-notification";
    }
    @GetMapping("/start-notification/{accountNumber}")
    public String startNotification(@PathVariable("accountNumber") String accountNumber){
        MeterAccountDetails meterAccountDetails = meterAccountDetailsRepository.findByAccountNumber(accountNumber);
        meterAccountDetails.setNotification(true);
        meterAccountDetailsRepository.save(meterAccountDetails);
        return "redirect:/customer-account/"+accountNumber;
    }
    @GetMapping("/stop-notification/{accountNumber}")
    public String stopNotification(@PathVariable("accountNumber") String accountNumber){
        MeterAccountDetails meterAccountDetails = meterAccountDetailsRepository.findByAccountNumber(accountNumber);
        meterAccountDetails.setNotification(false);
        meterAccountDetailsRepository.save(meterAccountDetails);
        return "redirect:/customer-account/"+accountNumber;
    }
}
