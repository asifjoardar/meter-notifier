package com.asif.meternotifier.controller;

import com.asif.meternotifier.service.CustomerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
public class ConfirmationTokenController {
    private final CustomerService customerService;

    public ConfirmationTokenController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @RequestMapping(value = "/confirm-account", method = {RequestMethod.GET, RequestMethod.POST})
    public String confirmUserAccount(@RequestParam("token") String confirmationToken) {
        try {
            customerService.confirmEmail(confirmationToken);
        } catch (Exception exception) {
            log.error(exception.getMessage());
        }
        return "email-verified";
    }

    @GetMapping("/email-verification/{id}")
    public String emailVerification(@PathVariable("id") Long id, Model model) {
        try {
            model.addAttribute("email", customerService.findCustomerById(id).getEmail());
            return "email-verification-message";
        } catch (Exception exception) {
            log.error(exception.getMessage());
            return "404";
        }
    }
}
