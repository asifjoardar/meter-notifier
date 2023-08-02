package com.asif.meternotifier.exception;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(NotFoundException.class)
    public String notFoundException(NotFoundException exception, Model model) {
        model.addAttribute("message", exception.getMessage());
        return "404";
    }

    @ExceptionHandler(EmailNotEnabledException.class)
    public String emailNotEnabledException(EmailNotEnabledException exception) {
        return "redirect:/email-verification/" + exception.getId();
    }
}
