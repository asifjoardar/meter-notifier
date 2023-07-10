package com.asif.meternotifier.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "account_details_id")
    private List<MeterAccountDetails>meterAccountDetailsList;

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
        email = email;
    }

    public List<MeterAccountDetails> getMeterAccountDetailsList() {
        return meterAccountDetailsList;
    }

    public void setMeterAccountDetailsList(List<MeterAccountDetails> meterAccountDetailsList) {
        this.meterAccountDetailsList = meterAccountDetailsList;
    }
}
