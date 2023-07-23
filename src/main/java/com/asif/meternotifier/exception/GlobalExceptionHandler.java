package com.asif.meternotifier.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(MeterNotFoundException.class)
    public String handleMeterNotFoundException(HttpServletRequest request,
                                               MeterNotFoundException meterNotFoundException,
                                               Model model) {
        model.addAttribute("message", meterNotFoundException.getMessage());
        return "404";
    }
    @ExceptionHandler(CustomerNotFoundException.class)
    public String handleCustomerNotFoundException(HttpServletRequest request,
                                               CustomerNotFoundException customerNotFoundException,
                                               Model model) {
        model.addAttribute("message", customerNotFoundException.getMessage());
        return "404";
    }
}
