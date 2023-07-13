package com.asif.meternotifier.controller;

import com.asif.meternotifier.dto.Data;
import com.asif.meternotifier.entity.Customer;
import com.asif.meternotifier.entity.MeterAccountDetails;
import com.asif.meternotifier.repository.MeterAccountDetailsRepository;
import com.asif.meternotifier.service.CustomerService;
import com.asif.meternotifier.util.RequestSender;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
public class AccountsController {

    private CustomerService customerService;
    private MeterAccountDetailsRepository meterAccountDetailsRepository;

    private RequestSender requestSender;
    public AccountsController(CustomerService customerService,
                              MeterAccountDetailsRepository meterAccountDetailsRepository,
                              RequestSender requestSender){
        this.customerService = customerService;
        this.meterAccountDetailsRepository = meterAccountDetailsRepository;
        this.requestSender = requestSender;
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
            return "signup";
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
        String url = "http://prepaid.desco.org.bd/api/tkdes/customer/getBalance?accountNo="+accountNumber+"&meterNo="+meterAccountDetails.getMeterNumber();
        ObjectMapper mapper = new ObjectMapper();
        Data data = mapper.treeToValue(requestSender.request(url), Data.class);
        model.addAttribute("customer", data);
        model.addAttribute("notification", meterAccountDetails.isNotification());

        return "customer-account-details";
    }
}
