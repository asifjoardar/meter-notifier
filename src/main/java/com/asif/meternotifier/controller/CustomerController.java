package com.asif.meternotifier.controller;

import com.asif.meternotifier.dto.ApiData;
import com.asif.meternotifier.dto.FormData;
import com.asif.meternotifier.entity.Customer;
import com.asif.meternotifier.exception.BadRequestException;
import com.asif.meternotifier.exception.NotFoundException;
import com.asif.meternotifier.service.ConfirmationTokenService;
import com.asif.meternotifier.service.CustomerService;
import com.asif.meternotifier.util.DataMapperUtil;
import com.asif.meternotifier.validation.Validation;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Slf4j
@Controller
public class CustomerController {
    private final CustomerService customerService;
    private final Validation validation;
    private final DataMapperUtil dataMapperUtil;
    private final ConfirmationTokenService confirmationTokenService;

    public CustomerController(CustomerService customerService,
                              Validation validation,
                              DataMapperUtil dataMapperUtil,
                              ConfirmationTokenService confirmationTokenService) {
        this.customerService = customerService;
        this.validation = validation;
        this.dataMapperUtil = dataMapperUtil;
        this.confirmationTokenService = confirmationTokenService;
    }

    @GetMapping("/")
    public String showSigninForm(Model model) {
        model.addAttribute("customer", new Customer());
        return "signin";
    }

    @PostMapping("/")
    public String signin(@Valid @ModelAttribute("customer") Customer customer,
                         BindingResult result,
                         Model model) {
        if (result.hasErrors()) {
            return "signin";
        }

        String message;

        try {
            final Long customerId = customerService.findCustomerByEmail(customer.getEmail()).getId();
            if (validation.emailEnabled(customerId)) {
                return "redirect:/customer-account-details/" + customerId;
            }
            return "redirect:/email-verification/" + customerId;
        } catch (NotFoundException exception) {
            message = exception.getMessage();
        } catch (Exception exception) {
            message = "Something went wrong, try again";
        }
        model.addAttribute("error", message);
        return "signin";
    }

    @GetMapping("/signup")
    public String showRegistrationForm(Model model) {
        model.addAttribute("signupFormData", new FormData());
        return "signup";
    }

    @PostMapping("/signup")
    public String registration(@Valid @ModelAttribute("signupFormData") FormData signupFormData,
                               BindingResult result,
                               Model model) {
        if (result.hasErrors()) {
            return "signup";
        }

        String message;

        try {
            final String acNo = signupFormData.getAccountNumber();
            final String meterNo = signupFormData.getMeterNumber();
            ApiData apiData = dataMapperUtil.getCustomerDataFromApi(acNo, meterNo);
            signupFormData.setBalance(apiData.getBalance());
            Customer customer = customerService.save(signupFormData);
            confirmationTokenService.generateAndSendToken(customer);
            return "redirect:/email-verification/" + customer.getId();
        } catch (NotFoundException | BadRequestException exception) {
            message = exception.getMessage();
        } catch (Exception exception) {
            message = "Something went wrong, try again";
            log.error(exception.getMessage());
        }
        model.addAttribute("error", message);
        return "signup";
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
