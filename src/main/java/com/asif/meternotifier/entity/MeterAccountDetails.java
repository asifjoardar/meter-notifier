package com.asif.meternotifier.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
public class MeterAccountDetails {

    @Id
    @NotEmpty
    private String accountNumber;
    @NotEmpty
    private String meterNumber;
    private Double balance;
    @OneToOne(targetEntity = Notification.class, fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(nullable = true, name = "notification_id")
    private Notification notification;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    public MeterAccountDetails() {
    }

    public MeterAccountDetails(String accountNumber,
                               String meterNumber,
                               Double balance,
                               Notification notification,
                               Customer customer) {
        this.accountNumber = accountNumber;
        this.meterNumber = meterNumber;
        this.balance = balance;
        this.notification = notification;
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

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public Notification getNotification() {
        return notification;
    }

    public void setNotification(Notification notification) {
        this.notification = notification;
    }

    @Override
    public String toString() {
        return "MeterAccountDetails{" +
                "accountNumber='" + accountNumber + '\'' +
                ", meterNumber='" + meterNumber + '\'' +
                ", balance=" + balance +
                ", notification=" + notification +
                ", customer=" + customer +
                '}';
    }
}
