package com.asif.meternotifier.entity;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String email;
    private boolean enabled;

    private boolean notification;
    private boolean notified;
    @OneToMany(cascade = CascadeType.ALL)
    //@JoinColumn(name = "customer_id")
    private List<MeterAccountDetails>meterAccountDetailsList = new ArrayList<>();

    public Customer() {
    }

    public Customer(Long id, String email, boolean enabled, boolean notification, boolean notified, List<MeterAccountDetails> meterAccountDetailsList) {
        this.id = id;
        this.email = email;
        this.enabled = enabled;
        this.notification = notification;
        this.notified = notified;
        this.meterAccountDetailsList = meterAccountDetailsList;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public List<MeterAccountDetails> getMeterAccountDetailsList() {
        return meterAccountDetailsList;
    }

    public void setMeterAccountDetailsList(List<MeterAccountDetails> meterAccountDetailsList) {
        this.meterAccountDetailsList = meterAccountDetailsList;
    }

    public boolean isNotified() {
        return notified;
    }

    public void setNotified(boolean notified) {
        this.notified = notified;
    }

    public boolean isNotification() {
        return notification;
    }

    public void setNotification(boolean notification) {
        this.notification = notification;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", enabled=" + enabled +
                ", notified=" + notified +
                ", meterAccountDetailsList=" + meterAccountDetailsList +
                '}';
    }
}
