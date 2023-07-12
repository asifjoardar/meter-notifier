package com.asif.meternotifier.entity;

import jakarta.persistence.*;

@Entity
public class MeterAccountDetails {

    @Id
    private String accountNumber;
    private String meterNumber;
    private boolean notification;
    private boolean notified;

    @ManyToOne(fetch = FetchType.LAZY)
    private Customer customer;

    public MeterAccountDetails() {
    }

    public MeterAccountDetails(String accountNumber, String meterNumber, boolean notification, boolean notified, Customer customer) {
        this.accountNumber = accountNumber;
        this.meterNumber = meterNumber;
        this.notification = notification;
        this.notified = notified;
        this.customer = customer;
    }

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

    public boolean isNotification() {
        return notification;
    }

    public void setNotification(boolean notification) {
        this.notification = notification;
    }

    public boolean isNotified() {
        return notified;
    }

    public void setNotified(boolean notified) {
        this.notified = notified;
    }

    @Override
    public String toString() {
        return "MeterAccountDetails{" +
                "accountNumber='" + accountNumber + '\'' +
                ", meterNumber='" + meterNumber + '\'' +
                ", notification=" + notification +
                ", notified=" + notified +
                ", customer=" + customer +
                '}';
    }
}
