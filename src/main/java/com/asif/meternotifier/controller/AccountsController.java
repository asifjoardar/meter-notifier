package com.asif.meternotifier.controller;

import com.asif.meternotifier.dto.Data;
import com.asif.meternotifier.entity.Customer;
import com.asif.meternotifier.entity.MeterAccountDetails;
import com.asif.meternotifier.repository.MeterAccountDetailsRepository;
import com.asif.meternotifier.service.CustomerService;
import com.asif.meternotifier.util.DataMapper;
import com.asif.meternotifier.util.RequestSender;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
public class AccountsController {
    private CustomerService customerService;
    private MeterAccountDetailsRepository meterAccountDetailsRepository;
    private RequestSender requestSender;
    private DataMapper dataMapper;
    public AccountsController(CustomerService customerService,
                              MeterAccountDetailsRepository meterAccountDetailsRepository,
                              RequestSender requestSender,
                              DataMapper dataMapper){
        this.customerService = customerService;
        this.meterAccountDetailsRepository = meterAccountDetailsRepository;
        this.requestSender = requestSender;
        this.dataMapper = dataMapper;
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
    @GetMapping("/customer-account-details/{id}")
    public String customerInfoDetails(@PathVariable("id") Long id, Model model){
        Customer customer = customerService.findCustomerById(id).get();
        model.addAttribute("customer", customer);
        return "customer-account-details";
    }
    @GetMapping("/add-meter/{id}")
    public String showAddMeter(@PathVariable("id") Long id, Model model){
        model.addAttribute("id", id);
        model.addAttribute("meterAccountDetails", new MeterAccountDetails());
        return "add-meter";
    }
    @PostMapping("/add-meter/{id}")
    public String addMeter(@PathVariable("id") Long id, MeterAccountDetails meterAccountDetails, Model model){
        customerService.saveCustomer(customerService.findCustomerById(id).get(), meterAccountDetails);
        return "redirect:/customer-account-details/{id}";
    }
    @GetMapping("edit-meter/{accountNumber}")
    public String showEditMeter(@PathVariable("accountNumber") String accountNumber, Model model){
        MeterAccountDetails meterAccountDetails = meterAccountDetailsRepository.findByAccountNumber(accountNumber);
        model.addAttribute("meterAccountDetails", meterAccountDetails);
        return "edit-meter";
    }
    @PostMapping("edit-meter/{accountNumber}")
    public String editMeter(@PathVariable("accountNumber") String accountNumber,
                            MeterAccountDetails meterAccountDetails,
                            Model model){
        //todo: update meter info
        return "redirect:/customer-account-details/{id}";
    }
    @GetMapping("delete-confirmation/{id}") /*path: /{id}/{generatedValue}/{acNo}/{MeterNo}*/
    public String deleteMeterConf(@PathVariable("id") Long id){
        return "delete-meter-confirmation";
    }
    @PostMapping("delete-meter/{id}/{accountNumber}/{meterNumber}")
    public String deleteMeter(@PathVariable("accountNumber") String accountNumber){
        //todo: delete a meter from the list
        return "redirect:/customer-account-details/{id}";
    }
}
