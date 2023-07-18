package com.asif.meternotifier.controller;

import com.asif.meternotifier.entity.Customer;
import com.asif.meternotifier.entity.MeterAccountDetails;
import com.asif.meternotifier.service.CustomerService;
import com.asif.meternotifier.validation.Validation;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
public class HomeController {

    private CustomerService customerService;
    private Validation validation;

    public HomeController(CustomerService customerService, Validation validation){
        this.customerService = customerService;
        this.validation = validation;
    }

    @GetMapping("/")
    public String showSigninForm(Customer customer){
        return "signin";
    }
    @PostMapping("/")
    public String signin(@Valid Customer customer, BindingResult result, Model model){
        if (result.hasErrors()) {
            return "signin";
        }
        if (validation.emailExist(customer.getEmail())){
            if(validation.emailEnabled(customerService.findCustomerByEmail(customer.getEmail()).getId())){
                return "redirect:/customer-account-details/"+customerService.findCustomerByEmail(customer.getEmail()).getId();
            } else{
                return "redirect:/email-verification/"+customerService.findCustomerByEmail(customer.getEmail()).getId();
            }
        } else {
            model.addAttribute("error", "We couldn't find an account with that email address");
            return "signin";
        }
    }
    @GetMapping("/signup")
    public String showRegistrationForm(Customer customer, MeterAccountDetails meterAccountDetails){
        return "signup";
    }
    @PostMapping("/signup")
    public String registration(@Valid Customer customer, BindingResult result, MeterAccountDetails meterAccountDetails, Model model){
        if (result.hasErrors()) {
            return "signup";
        }
        if(!validation.accountMeterExist(meterAccountDetails.getAccountNumber(), meterAccountDetails.getMeterNumber())) {
            customerService.saveCustomer(customer, meterAccountDetails);
            return "redirect:/email-verification/"+customer.getId();
        } else{
            model.addAttribute("error", "Entered account / meter no already in use");
            return "signup";
        }
    }

    @GetMapping("/email-verification/{id}")
    public String emailVerification(@PathVariable("id") Long id, Model model){
        model.addAttribute("email", customerService.findCustomerById(id).get().getEmail());
        return "email-verification-message";
    }
}
