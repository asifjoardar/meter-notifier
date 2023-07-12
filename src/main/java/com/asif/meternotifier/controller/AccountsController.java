package com.asif.meternotifier.controller;

import com.asif.meternotifier.entity.Customer;
import com.asif.meternotifier.entity.MeterAccountDetails;
import com.asif.meternotifier.repository.MeterAccountDetailsRepository;
import com.asif.meternotifier.service.CustomerService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Controller
public class AccountsController {

    private CustomerService customerService;
    private MeterAccountDetailsRepository meterAccountDetailsRepository;
    public AccountsController(CustomerService customerService, MeterAccountDetailsRepository meterAccountDetailsRepository){
        this.customerService = customerService;
        this.meterAccountDetailsRepository = meterAccountDetailsRepository;
    }

    @RequestMapping(value="/confirm-account", method= {RequestMethod.GET, RequestMethod.POST})
    public String confirmUserAccount(@RequestParam("token")String confirmationToken) {
        try {
            if(customerService.confirmEmail(confirmationToken)){
                return "redirect:/";
            }
            else {
                throw new Exception("Token is not valid");
            }

        } catch (Exception e){
            System.out.println(e);
            return "registration";
        }
    }

    @GetMapping("/customer-info/{id}")
    public String customerInfo(@PathVariable(value = "id") Long id, Model model){
        Optional<Customer> customer = customerService.findCustomerById(id);
        model.addAttribute("accountDetails", customer.get().getMeterAccountDetailsList());
        System.out.println(customer.get().getMeterAccountDetailsList());
        return "customer-info";
    }

    @GetMapping("/customer-account/{accountNumber}")
    public String customerInfoDetails(@PathVariable("accountNumber") String accountNumber, Model model) throws JsonProcessingException {
        MeterAccountDetails meterAccountDetails = meterAccountDetailsRepository.findByAccountNumber(accountNumber);
        /*String url = "http://prepaid.desco.org.bd/api/tkdes/customer/getBalance?accountNo="+accountNumber+"&meterNo="+meterAccountDetails.getMeterNumber();
        Map<String, Object> response = requestSender.request(url);
        model.addAttribute("customer", response.get("data"));*/
        Map<String, String> data = new HashMap<>();
        data.put("accountNo", "12042012");
        data.put("meterNo", "663110109424");
        data.put("balance", "1369.58");
        data.put("currentMonthConsumption", "1249.35");
        data.put("readingTime", "2023-07-11 00:00:00");

        model.addAttribute("customer", data);
        model.addAttribute("notification", meterAccountDetails.isNotification());

        return "customer-account-details";
    }
}