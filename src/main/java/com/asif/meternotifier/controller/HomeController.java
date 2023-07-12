package com.asif.meternotifier.controller;

import com.asif.meternotifier.dto.CustomerDto;
import com.asif.meternotifier.dto.LoginDto;
import com.asif.meternotifier.service.CustomerService;
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
    public String showLoginForm(Model model){
        model.addAttribute("login", new LoginDto());
        return "login";
    }
    @PostMapping("/")
    public String login(@ModelAttribute(value = "login") LoginDto loginDto){
        //if(customerService.findCustomerByEmail(loginDto.getEmail()) != null && customerService.findCustomerByEmail(loginDto.getEmail()).isEnabled())
        if(customerService.findCustomerByEmail(loginDto.getEmail()) != null){
            return "redirect:/customer-info/"+customerService.findCustomerByEmail(loginDto.getEmail()).getId();
        } else {
            return "redirect:/";
        }
    }
    @GetMapping("/registration")
    public String showRegistrationForm(Model model){
        model.addAttribute("customer", new CustomerDto());
        return "registration";
    }
    @PostMapping("/registration")
    public String registration(@ModelAttribute(value = "customer") CustomerDto customerDto,
                               BindingResult result,
                               Model model){
        customerService.saveCustomer(customerDto);
        return "redirect:/";
    }
}
