package com.asif.meternotifier.controller;

import com.asif.meternotifier.dto.ApiData;
import com.asif.meternotifier.dto.FormData;
import com.asif.meternotifier.entity.Customer;
import com.asif.meternotifier.service.ConfirmationTokenService;
import com.asif.meternotifier.service.CustomerService;
import com.asif.meternotifier.service.impl.FormDataService;
import com.asif.meternotifier.util.DataMapperUtil;
import com.asif.meternotifier.validation.Validation;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class CustomerController {
    private final CustomerService customerService;
    private final Validation validation;
    private final DataMapperUtil dataMapperUtil;
    private final FormDataService dataService;
    private final ConfirmationTokenService confirmationTokenService;

    public CustomerController(CustomerService customerService,
                              Validation validation,
                              DataMapperUtil dataMapperUtil,
                              FormDataService dataService,
                              ConfirmationTokenService confirmationTokenService) {
        this.customerService = customerService;
        this.validation = validation;
        this.dataMapperUtil = dataMapperUtil;
        this.dataService = dataService;
        this.confirmationTokenService = confirmationTokenService;
    }

    @GetMapping("/")
    public String showSigninForm(Model model) {
        model.addAttribute("customer", new Customer());
        return "signin";
    }

    @PostMapping("/")
    public String signin(@Valid @ModelAttribute("customer") Customer customer, BindingResult result, Model model) {
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
    public String showRegistrationForm(Model model) {
        model.addAttribute("signupFormData", new FormData());
        return "signup";
    }

    @PostMapping("/signup")
    public String registration(@Valid @ModelAttribute("signupFormData") FormData signupFormData,
                               BindingResult result, Model model) throws JsonProcessingException {
        if (result.hasErrors()) {
            return "signup";
        }

        final String acNo = signupFormData.getAccountNumber();
        final String meterNo = signupFormData.getMeterNumber();

        if (validation.emailExist(signupFormData.getEmail())) {
            model.addAttribute("error", "Email address is already in use");
            return "signup";
        } else if (!validation.accountMeterExist(acNo, meterNo)) {
            ApiData data = dataMapperUtil.getDataFromMapper(acNo, meterNo);
            if (data == null) {
                model.addAttribute("error", "The Account No. does not exist");
                return "signup";
            } else {
                signupFormData.setBalance(data.getBalance());
                Customer customer = dataService.saveFormDataToTables(signupFormData);
                confirmationTokenService.generateAndSendToken(customer);
                return "redirect:/email-verification/" + customer.getId();
            }
        } else {
            model.addAttribute("error", "Entered account / meter no already in use");
            return "signup";
        }
    }

    @GetMapping("/customer-account-details/{id}")
    public String customerInfoDetails(@PathVariable("id") Long id, Model model) {
        if (validation.emailEnabled(id)) {
            Customer customer = customerService.findCustomerById(id);
            model.addAttribute("customer", customer);
            return "customer-account-details";
        } else {
            return "redirect:/email-verification/" + id;
        }
    }
}
