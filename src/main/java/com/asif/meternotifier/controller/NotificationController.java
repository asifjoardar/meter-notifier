package com.asif.meternotifier.controller;

import com.asif.meternotifier.entity.MeterAccountDetails;
import com.asif.meternotifier.repository.MeterAccountDetailsRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class NotificationController {
    private MeterAccountDetailsRepository meterAccountDetailsRepository;
    public NotificationController(MeterAccountDetailsRepository meterAccountDetailsRepository){
        this.meterAccountDetailsRepository = meterAccountDetailsRepository;
    }
}
