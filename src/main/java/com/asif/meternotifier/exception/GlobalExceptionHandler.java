package com.asif.meternotifier.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(NotFoundException.class)
    public String handleNotFoundException(HttpServletRequest request,
                                          NotFoundException notFoundException,
                                          Model model){
        model.addAttribute("message", notFoundException.getMessage());
        return "404";
    }
}
