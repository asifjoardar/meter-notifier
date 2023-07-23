package com.asif.meternotifier.controller;

import com.asif.meternotifier.dto.Data;
import com.asif.meternotifier.entity.Customer;
import com.asif.meternotifier.entity.MeterAccountDetails;
import com.asif.meternotifier.repository.MeterAccountDetailsRepository;
import com.asif.meternotifier.service.CustomerService;
import com.asif.meternotifier.util.DataMapper;
import com.asif.meternotifier.validation.Validation;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@Transactional
public class AccountsController {
    private CustomerService customerService;
    private MeterAccountDetailsRepository meterAccountDetailsRepository;
    private Validation validation;
    private DataMapper dataMapper;

    public AccountsController(CustomerService customerService,
                              MeterAccountDetailsRepository meterAccountDetailsRepository,
                              Validation validation,
                              DataMapper dataMapper) {
        this.customerService = customerService;
        this.meterAccountDetailsRepository = meterAccountDetailsRepository;
        this.validation = validation;
        this.dataMapper = dataMapper;
    }

    @RequestMapping(value = "/confirm-account", method = {RequestMethod.GET, RequestMethod.POST})
    public String confirmUserAccount(@RequestParam("token") String confirmationToken) {
        if (customerService.confirmEmail(confirmationToken)) {
            return "email-verified";
        } else {
            return "404";
        }
    }

    @GetMapping("/customer-account-details/{id}")
    public String customerInfoDetails(@PathVariable("id") Long id, Model model) {
        if (validation.emailEnabled(id)) {
            Customer customer = customerService.findCustomerById(id).get();
            model.addAttribute("customer", customer);
            return "customer-account-details";
        } else {
            return "redirect:/email-verification/" + id;
        }
    }

    @GetMapping("/add-meter/{id}")
    public String showAddMeter(@PathVariable("id") Long id, Model model) {
        if (validation.emailEnabled(id)) {
            model.addAttribute("id", id);
            model.addAttribute("meterAccountDetails", new MeterAccountDetails());
            return "add-meter";
        } else {
            return "redirect:/email-verification/" + id;
        }
    }

    @PostMapping("/add-meter/{id}")
    public String addMeter(@PathVariable("id") Long id,
                           MeterAccountDetails meterAccountDetails,
                           Model model) throws JsonProcessingException {
        final String acNo = meterAccountDetails.getAccountNumber();
        final String meterNo = meterAccountDetails.getMeterNumber();
        if (!validation.accountMeterExist(acNo, meterNo)) {
            Data data = dataMapper.getDataFromMapper(acNo, meterNo);
            if (data == null) {
                model.addAttribute("error", "The Account No. does not exist");
                return "add-meter";
            } else {
                meterAccountDetails.setBalance(data.getBalance());
                customerService.saveCustomer(customerService.findCustomerById(id).get(), meterAccountDetails);
                return "redirect:/customer-account-details/{id}";
            }
        } else {
            model.addAttribute("error", "Entered account / meter no already in use");
            return "add-meter";
        }
    }

    @GetMapping("edit-meter/{accountNumber}")
    public String showEditMeter(@PathVariable("accountNumber") String accountNumber, Model model) {
        MeterAccountDetails meterAccountDetails = meterAccountDetailsRepository.findByAccountNumber(accountNumber);
        if (validation.emailEnabled(meterAccountDetails.getCustomer().getId())) {
            model.addAttribute("meterAccountDetails", meterAccountDetails);
            return "edit-meter";
        } else {
            return "redirect:/email-verification/" + meterAccountDetails.getCustomer().getId();
        }
    }

    @PostMapping("edit-meter/{accountNumber}")
    public String editMeter(@PathVariable("accountNumber") String accountNumber,
                            MeterAccountDetails meterAccountDetails,
                            Model model) {
        customerService.updateCustomer(meterAccountDetails);
        Customer customer = meterAccountDetailsRepository.findByAccountNumber(meterAccountDetails.getAccountNumber()).getCustomer();
        return "redirect:/customer-account-details/" + customer.getId();
    }

    @GetMapping("delete-confirmation/{accountNumber}")
    public String deleteMeterConf(@PathVariable("accountNumber") String accountNumber, Model model) {
        Customer customer = meterAccountDetailsRepository.findByAccountNumber(accountNumber).getCustomer();
        if (validation.emailEnabled(customer.getId())) {
            model.addAttribute("accountNumber", accountNumber);
            model.addAttribute("id", customer.getId());
            return "delete-meter-confirmation";
        } else {
            return "redirect:/email-verification/" + customer.getId();
        }
    }

    @PostMapping("delete-meter/{accountNumber}")
    public String deleteMeter(@PathVariable("accountNumber") String accountNumber) {
        Customer customer = meterAccountDetailsRepository.findByAccountNumber(accountNumber).getCustomer();
        meterAccountDetailsRepository.deleteByAccountNumber(accountNumber);
        return "redirect:/customer-account-details/" + customer.getId();
    }
}
