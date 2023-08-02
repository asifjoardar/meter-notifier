package com.asif.meternotifier.controller;

import com.asif.meternotifier.annotations.RequiresEnabledEmail;
import com.asif.meternotifier.dto.ApiDataDto;
import com.asif.meternotifier.entity.Customer;
import com.asif.meternotifier.entity.Meter;
import com.asif.meternotifier.entity.Notification;
import com.asif.meternotifier.exception.BadRequestException;
import com.asif.meternotifier.exception.NotFoundException;
import com.asif.meternotifier.service.CustomerService;
import com.asif.meternotifier.service.MeterService;
import com.asif.meternotifier.service.NotificationService;
import com.asif.meternotifier.util.DataMapperUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
public class MeterController {
    private final CustomerService customerService;
    private final MeterService meterService;
    private final DataMapperUtil dataMapperUtil;
    private final NotificationService notificationService;

    public MeterController(CustomerService customerService,
                           MeterService meterService,
                           DataMapperUtil dataMapperUtil,
                           NotificationService notificationService) {
        this.customerService = customerService;
        this.meterService = meterService;
        this.dataMapperUtil = dataMapperUtil;
        this.notificationService = notificationService;
    }

    @RequiresEnabledEmail
    @GetMapping("/add-meter/{id}")
    public String showAddMeter(@PathVariable("id") Long id, Model model) {
        model.addAttribute("id", id);
        model.addAttribute("meterAccountDetails", new Meter());
        return "add-meter";
    }

    @PostMapping("/add-meter/{id}")
    public String addMeter(@PathVariable("id") Long id,
                           @ModelAttribute("meterAccountDetails") Meter meter,
                           Model model) {
        String message;
        try {
            final String acNo = meter.getAccountNumber();
            final String meterNo = meter.getMeterNumber();
            ApiDataDto apiData = dataMapperUtil.getCustomerDataFromApi(acNo, meterNo);
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

    @RequiresEnabledEmail
    @GetMapping("edit-meter/{id}/{accountNumber}")
    public String showEditMeter(@PathVariable("id") Long id,
                                @PathVariable("accountNumber") String accountNumber,
                                Model model) {
        Customer customer = customerService.findCustomerById(id);
        Meter meter = meterService.findByAccountNumber(accountNumber);
        model.addAttribute("customer", customer);
        model.addAttribute("meter", meter);
        model.addAttribute("notification", meter.getNotification());
        return "edit-meter";
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

    @RequiresEnabledEmail
    @GetMapping("delete-confirmation/{id}/{accountNumber}")
    public String deleteMeterConf(@PathVariable("accountNumber") String accountNumber,
                                  @PathVariable("id") Long id, Model model) {
        model.addAttribute("accountNumber", accountNumber);
        model.addAttribute("id", id);
        return "delete-meter-confirmation";
    }

    @PostMapping("delete-meter/{id}/{accountNumber}")
    public String deleteMeter(@PathVariable("accountNumber") String accountNumber,
                              @PathVariable("id") Long id) {
        meterService.deleteByAccountNumber(accountNumber);
        return "redirect:/customer-account-details/" + id;
    }
}
