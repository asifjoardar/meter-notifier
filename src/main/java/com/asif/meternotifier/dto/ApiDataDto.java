package com.asif.meternotifier.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApiDataDto {
    private String accountNo;
    private String meterNo;
    private double balance;
    private String currentMonthConsumption;
    private String readingTime;
}
