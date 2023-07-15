package com.asif.meternotifier.controller;

import com.asif.meternotifier.entity.Customer;
import com.asif.meternotifier.entity.MeterAccountDetails;
import com.asif.meternotifier.service.CustomerService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
public class HomeController {

    private CustomerService customerService;

    public HomeController(CustomerService customerService){
        this.customerService = customerService;
    }

    @GetMapping("/")
    public String showLoginForm(Customer customer){
        return "signin";
    }
    @PostMapping("/")
    public String login(@Valid Customer customer, BindingResult result, Model model){
        if (result.hasErrors()) {
            return "signin";
        }
        /*if (customerService.findCustomerByEmail(customer.getEmail()).isEnabled()){
            return "redirect:/customer-info/"+customer.getId();
        }*/
        if (customerService.findCustomerByEmail(customer.getEmail()) != null){
            return "redirect:/customer-info/"+customerService.findCustomerByEmail(customer.getEmail()).getId();
        }
        return "redirect:/";
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
        customerService.saveCustomer(customer, meterAccountDetails);
        return "redirect:/";
    }
}
