package com.asif.meternotifier.controller;

import com.asif.meternotifier.dto.ApiData;
import com.asif.meternotifier.dto.FormData;
import com.asif.meternotifier.entity.Customer;
import com.asif.meternotifier.entity.Meter;
import com.asif.meternotifier.entity.Notification;
import com.asif.meternotifier.service.CustomerService;
import com.asif.meternotifier.service.MeterService;
import com.asif.meternotifier.service.impl.FormDataService;
import com.asif.meternotifier.util.DataMapperUtil;
import com.asif.meternotifier.validation.Validation;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class MeterController {
    private final CustomerService customerService;
    private final MeterService meterAccountDetailsService;
    private final Validation validation;
    private final DataMapperUtil dataMapperUtil;
    private final FormDataService dataService;

    public MeterController(CustomerService customerService,
                           MeterService meterAccountDetailsService,
                           Validation validation,
                           DataMapperUtil dataMapperUtil,
                           FormDataService dataService) {
        this.customerService = customerService;
        this.meterAccountDetailsService = meterAccountDetailsService;
        this.validation = validation;
        this.dataMapperUtil = dataMapperUtil;
        this.dataService = dataService;
    }

    @GetMapping("/add-meter/{id}")
    public String showAddMeter(@PathVariable("id") Long id, Model model) {
        if (validation.emailEnabled(id)) {
            model.addAttribute("id", id);
            model.addAttribute("meterAccountDetails", new Meter());
            return "add-meter";
        } else {
            return "redirect:/email-verification/" + id;
        }
    }

    @PostMapping("/add-meter/{id}")
    public String addMeter(@PathVariable("id") Long id,
                           @ModelAttribute("meterAccountDetails") Meter meterAccountDetails,
                           Model model) throws JsonProcessingException {
        final String acNo = meterAccountDetails.getAccountNumber();
        final String meterNo = meterAccountDetails.getMeterNumber();
        if (!validation.accountMeterExist(acNo, meterNo)) {
            ApiData data = dataMapperUtil.getDataFromMapper(acNo, meterNo);
            if (data == null) {
                model.addAttribute("error", "The Account No. does not exist");
                return "add-meter";
            } else {
                Customer customer = customerService.findCustomerById(id);
                meterAccountDetails.setBalance(data.getBalance());
                meterAccountDetails.setNotification(new Notification());
                meterAccountDetails.setCustomer(customer);
                meterAccountDetailsService.save(meterAccountDetails);
                customer.getMeterAccountDetailsList().add(meterAccountDetails);
                customerService.save(customer);
                return "redirect:/customer-account-details/{id}";
            }
        } else {
            model.addAttribute("error", "Entered account / meter no already in use");
            return "add-meter";
        }
    }

    @GetMapping("edit-meter/{id}/{accountNumber}")
    public String showEditMeter(@PathVariable("id") Long id,
                                @PathVariable("accountNumber") String accountNumber,
                                Model model) {
        Meter meterAccountDetails = meterAccountDetailsService.findByAccountNumber(accountNumber);
        FormData formData = dataMapperUtil.dataMappingByAccountNo(meterAccountDetails);
        if (validation.emailEnabled(meterAccountDetails.getCustomer().getId())) {
            model.addAttribute("id", id);
            model.addAttribute("formData", formData);
            return "edit-meter";
        } else {
            return "redirect:/email-verification/" + id;
        }
    }

    @PostMapping("edit-meter/{id}/{accountNumber}")
    public String editMeter(@PathVariable("id") String id,
                            @ModelAttribute("formData") FormData formData) {
        dataService.updateFormDataToTables(formData);
        return "redirect:/customer-account-details/" + id;
    }

    @GetMapping("delete-confirmation/{accountNumber}")
    public String deleteMeterConf(@PathVariable("accountNumber") String accountNumber, Model model) {
        Customer customer = meterAccountDetailsService.findByAccountNumber(accountNumber).getCustomer();
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
        Customer customer = meterAccountDetailsService.findByAccountNumber(accountNumber).getCustomer();
        meterAccountDetailsService.deleteByAccountNumber(accountNumber);
        return "redirect:/customer-account-details/" + customer.getId();
    }
}
