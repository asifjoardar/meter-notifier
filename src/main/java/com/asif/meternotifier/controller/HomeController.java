package com.asif.meternotifier.controller;

import com.asif.meternotifier.dto.CustomerDto;
import com.asif.meternotifier.entity.Customer;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;

@Controller
public class HomeController {
    @GetMapping("/")
    public String showLoginForm(){
        return "login";
    }
    @PostMapping("/")
    public String login(){
        return "todo";
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
        System.out.println(customerDto.getEmail());
        System.out.println(customerDto.getAccountNumber());
        System.out.println(customerDto.getMeterNumber());
        return "redirect:/";
    }
}
