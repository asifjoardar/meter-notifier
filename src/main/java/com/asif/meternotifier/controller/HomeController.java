package com.asif.meternotifier.controller;

import com.asif.meternotifier.dto.CustomerDto;
import com.asif.meternotifier.dto.LoginDto;
import com.asif.meternotifier.service.CustomerService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class HomeController {

    private CustomerService customerService;

    public HomeController(CustomerService customerService){
        this.customerService = customerService;
    }

    @GetMapping("/")
    public String showLoginForm(Model model){
        model.addAttribute("signin", new LoginDto());
        return "signin";
    }
    @PostMapping("/")
    public String login(@ModelAttribute(value = "signin") LoginDto loginDto){
        if(customerService.findCustomerByEmail(loginDto.getEmail()) != null){
            if(customerService.findCustomerByEmail(loginDto.getEmail()).isEnabled()){
                return "redirect:/customer-info/"+customerService.findCustomerByEmail(loginDto.getEmail()).getId();
            } else{
                return "redirect:/";
            }
        } else {
            return "redirect:/";
        }
    }
    @GetMapping("/signup")
    public String showRegistrationForm(Model model){
        model.addAttribute("customer", new CustomerDto());
        return "signup";
    }
    @PostMapping("/signup")
    public String registration(@ModelAttribute(value = "customer") CustomerDto customerDto){
        customerService.saveCustomer(customerDto);
        return "redirect:/";
    }
}
