package com.asif.meternotifier.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private boolean status;
    private boolean notified;
    private Double minimumBalance;
    private String emailToSendNotification;

    public Notification() {
    }

    public Notification(Long id, boolean status, boolean notified, Double minimumBalance, String emailToSendNotification) {
        this.id = id;
        this.status = status;
        this.notified = notified;
        this.minimumBalance = minimumBalance;
        this.emailToSendNotification = emailToSendNotification;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public boolean isNotified() {
        return notified;
    }

    public void setNotified(boolean notified) {
        this.notified = notified;
    }

    public Double getMinimumBalance() {
        return minimumBalance;
    }

    public void setMinimumBalance(Double minimumBalance) {
        this.minimumBalance = minimumBalance;
    }

    public String getEmailToSendNotification() {
        return emailToSendNotification;
    }

    public void setEmailToSendNotification(String emailToSendNotification) {
        this.emailToSendNotification = emailToSendNotification;
    }

    @Override
    public String toString() {
        return "Notification{" +
                "id=" + id +
                ", status=" + status +
                ", notified=" + notified +
                ", minimumBalance=" + minimumBalance +
                ", emailToSendNotification='" + emailToSendNotification + '\'' +
                '}';
    }
}
