package com.asif.meternotifier.controller;

import com.asif.meternotifier.service.CustomerService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class ConfirmationTokenController {
    private final CustomerService customerService;

    public ConfirmationTokenController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @RequestMapping(value = "/confirm-account", method = {RequestMethod.GET, RequestMethod.POST})
    public String confirmUserAccount(@RequestParam("token") String confirmationToken) {
        if (customerService.confirmEmail(confirmationToken)) {
            return "email-verified";
        } else {
            return "404";
        }
    }

    @GetMapping("/email-verification/{id}")
    public String emailVerification(@PathVariable("id") Long id, Model model) {
        model.addAttribute("email", customerService.findCustomerById(id).getEmail());
        return "email-verification-message";
    }
}
