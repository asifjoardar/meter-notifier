package com.asif.meternotifier.controller;

import com.asif.meternotifier.dto.ApiData;
import com.asif.meternotifier.entity.Customer;
import com.asif.meternotifier.entity.Meter;
import com.asif.meternotifier.entity.Notification;
import com.asif.meternotifier.exception.BadRequestException;
import com.asif.meternotifier.exception.NotFoundException;
import com.asif.meternotifier.service.CustomerService;
import com.asif.meternotifier.service.MeterService;
import com.asif.meternotifier.service.NotificationService;
import com.asif.meternotifier.util.DataMapperUtil;
import com.asif.meternotifier.validation.Validation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
public class MeterController {
    private final CustomerService customerService;
    private final MeterService meterService;
    private final Validation validation;
    private final DataMapperUtil dataMapperUtil;
    private final NotificationService notificationService;

    public MeterController(CustomerService customerService,
                           MeterService meterService,
                           Validation validation,
                           DataMapperUtil dataMapperUtil,
                           NotificationService notificationService) {
        this.customerService = customerService;
        this.meterService = meterService;
        this.validation = validation;
        this.dataMapperUtil = dataMapperUtil;
        this.notificationService = notificationService;
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
                           @ModelAttribute("meterAccountDetails") Meter meter,
                           Model model) {
        String message;
        try {
            final String acNo = meter.getAccountNumber();
            final String meterNo = meter.getMeterNumber();
            ApiData apiData = dataMapperUtil.getCustomerDataFromApi(acNo, meterNo);
            Customer customer = customerService.findCustomerById(id);
            meter.setBalance(apiData.getBalance());
            customerService.addNewMeter(customer, meter);
            return "redirect:/customer-account-details/{id}";
        } catch (NotFoundException | BadRequestException exception) {
            message = exception.getMessage();
        } catch (Exception exception) {
            message = "Something went wrong, try again";
            log.error(exception.getMessage());
        }
        model.addAttribute("error", message);
        return "add-meter";
    }

    @GetMapping("edit-meter/{id}/{accountNumber}")
    public String showEditMeter(@PathVariable("id") Long id,
                                @PathVariable("accountNumber") String accountNumber,
                                Model model) {
        Customer customer = customerService.findCustomerById(id);
        Meter meter = meterService.findByAccountNumber(accountNumber);
        if (validation.emailEnabled(id)) {
            model.addAttribute("customer", customer);
            model.addAttribute("meter", meter);
            model.addAttribute("notification", meter.getNotification());
            return "edit-meter";
        } else {
            return "redirect:/email-verification/" + id;
        }
    }

    @PostMapping("edit-meter/{id}/{accountNumber}")
    public String editMeter(@PathVariable("id") String id,
                            @PathVariable("accountNumber") String accountNumber,
                            @ModelAttribute("notification") Notification notification) {
        Meter meter = meterService.findByAccountNumber(accountNumber);
        notification.setId(meter.getNotification().getId());
        notificationService.save(notification);
        return "redirect:/customer-account-details/" + id;
    }

    @GetMapping("delete-confirmation/{accountNumber}")
    public String deleteMeterConf(@PathVariable("accountNumber") String accountNumber, Model model) {
        Customer customer = meterService.findByAccountNumber(accountNumber).getCustomer();
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
        Customer customer = meterService.findByAccountNumber(accountNumber).getCustomer();
        meterService.deleteByAccountNumber(accountNumber);
        return "redirect:/customer-account-details/" + customer.getId();
    }
}
