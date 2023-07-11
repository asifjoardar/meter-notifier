package com.asif.meternotifier.entity;

import jakarta.persistence.*;

@Entity
public class MeterAccountDetails {
    /*@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;*/

    @Id
    private String accountNumber;
    private String meterNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    private Customer customer;

    public MeterAccountDetails() {
    }

    public MeterAccountDetails(String accountNumber, String meterNumber) {
        this.accountNumber = accountNumber;
        this.meterNumber = meterNumber;
    }

    /*public Long getId() {
        return id;
    }
-
    public void setId(Long id) {
        this.id = id;
    }*/

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getMeterNumber() {
        return meterNumber;
    }

    public void setMeterNumber(String meterNumber) {
        this.meterNumber = meterNumber;
    }

    @Override
    public String toString() {
        return "MeterAccountDetails{" +
                "accountNumber='" + accountNumber + '\'' +
                ", meterNumber='" + meterNumber + '\'' +
                '}';
    }
}
