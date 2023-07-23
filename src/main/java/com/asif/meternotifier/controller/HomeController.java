package com.asif.meternotifier.controller;

import com.asif.meternotifier.dto.Data;
import com.asif.meternotifier.entity.Customer;
import com.asif.meternotifier.entity.MeterAccountDetails;
import com.asif.meternotifier.service.CustomerService;
import com.asif.meternotifier.util.DataMapperUtil;
import com.asif.meternotifier.validation.Validation;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
public class HomeController {

    private final CustomerService customerService;
    private final Validation validation;
    private final DataMapperUtil dataMapperUtil;

    public HomeController(CustomerService customerService,
                          Validation validation,
                          DataMapperUtil dataMapperUtil) {
        this.customerService = customerService;
        this.validation = validation;
        this.dataMapperUtil = dataMapperUtil;
    }

    @GetMapping("/")
    public String showSigninForm(Customer customer) {
        return "signin";
    }

    @PostMapping("/")
    public String signin(@Valid Customer customer, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "signin";
        }
        if (validation.emailExist(customer.getEmail())) {
            final Long customerId = customerService.findCustomerByEmail(customer.getEmail()).getId();
            if (validation.emailEnabled(customerId)) {
                return "redirect:/customer-account-details/" + customerId;
            } else {
                return "redirect:/email-verification/" + customerId;
            }
        } else {
            model.addAttribute("error", "We couldn't find an account with that email address");
            return "signin";
        }
    }

    @GetMapping("/signup")
    public String showRegistrationForm(Customer customer, MeterAccountDetails meterAccountDetails) {
        return "signup";
    }

    @PostMapping("/signup")
    public String registration(@Valid Customer customer,
                               BindingResult result,
                               MeterAccountDetails meterAccountDetails,
                               Model model) throws JsonProcessingException {
        if (result.hasErrors()) {
            return "signup";
        }
        final String acNo = meterAccountDetails.getAccountNumber();
        final String meterNo = meterAccountDetails.getMeterNumber();
        if (validation.emailExist(customer.getEmail())) {
            model.addAttribute("error", "Email address is already in use");
            return "signup";
        } else if (!validation.accountMeterExist(acNo, meterNo)) {
            Data data = dataMapperUtil.getDataFromMapper(acNo, meterNo);
            if (data == null) {
                model.addAttribute("error", "The Account No. does not exist");
                return "signup";
            } else {
                meterAccountDetails.setBalance(data.getBalance());
                customerService.saveCustomer(customer, meterAccountDetails);
                return "redirect:/email-verification/" + customer.getId();
            }
        } else {
            model.addAttribute("error", "Entered account / meter no already in use");
            return "signup";
        }
    }

    @GetMapping("/email-verification/{id}")
    public String emailVerification(@PathVariable("id") Long id, Model model) {
        model.addAttribute("email", customerService.findCustomerById(id).getEmail());
        return "email-verification-message";
    }
}
