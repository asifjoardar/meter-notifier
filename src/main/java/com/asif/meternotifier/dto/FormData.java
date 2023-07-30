package com.asif.meternotifier.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class FormData {
    @Email
    private String email;
    @NotEmpty
    private String accountNumber;
    @NotEmpty
    private String meterNumber;
    private double balance;
    private boolean notificationStatus;
    private double minBalance;
    private String sendNotificationTo;
}
