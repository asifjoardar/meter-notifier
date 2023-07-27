package com.asif.meternotifier.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignupFormData {
    @Email
    private String email;
    @NotEmpty
    private String accountNumber;
    @NotEmpty
    private String meterNumber;
    private Double balance;
}
