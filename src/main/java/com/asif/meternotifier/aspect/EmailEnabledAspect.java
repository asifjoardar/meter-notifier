package com.asif.meternotifier.aspect;

import com.asif.meternotifier.annotations.RequiresEnabledEmail;
import com.asif.meternotifier.exception.EmailNotEnabledException;
import com.asif.meternotifier.service.CustomerService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class EmailEnabledAspect {
    private final CustomerService customerService;

    public EmailEnabledAspect(CustomerService customerService) {
        this.customerService = customerService;
    }

    @Before("@annotation(requiresEnabledEmail)")
    public void validateEmail(JoinPoint joinPoint, RequiresEnabledEmail requiresEnabledEmail) {
        Object[] args = joinPoint.getArgs();
        for (Object arg : args) {
            if (arg instanceof Long id) {
                if (!customerService.findCustomerById(id).isEnabled()) {
                    throw new EmailNotEnabledException("Email is not enabled", id);
                }
                break;
            }
        }
    }
}
