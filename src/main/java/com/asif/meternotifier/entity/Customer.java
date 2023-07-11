package com.asif.meternotifier.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String email;
    private boolean isEnabled;
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "customer_id")
    private List<MeterAccountDetails>meterAccountDetailsList;

    public Customer() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Customer(String email, List<MeterAccountDetails> meterAccountDetailsList) {
        this.email = email;
        this.meterAccountDetailsList = meterAccountDetailsList;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<MeterAccountDetails> getMeterAccountDetailsList() {
        return meterAccountDetailsList;
    }

    public void setMeterAccountDetailsList(List<MeterAccountDetails> meterAccountDetailsList) {
        this.meterAccountDetailsList = meterAccountDetailsList;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", isEnabled=" + isEnabled +
                ", meterAccountDetailsList=" + meterAccountDetailsList +
                '}';
    }
}
