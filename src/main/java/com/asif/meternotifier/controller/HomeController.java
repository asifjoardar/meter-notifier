package com.asif.meternotifier.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class HomeController {
    @GetMapping("/about")
    public String showAboutPage() {
        return "about";
    }
}
