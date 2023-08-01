package com.asif.meternotifier.repository;

import com.asif.meternotifier.entity.Meter;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
class MeterAccountDetailsRepositoryTest {

    @Autowired
    private MeterRepository meterAccountDetailsTestRepository;

    @AfterEach
    void tearDown() {
        meterAccountDetailsTestRepository.deleteAll();
    }

    @Test
    void findByAccountNumber() {
        Meter meterAccountDetails = new Meter();
        meterAccountDetails.setAccountNumber("12345");
        meterAccountDetails.setMeterNumber("54321");
        meterAccountDetailsTestRepository.save(meterAccountDetails);
        boolean expected = meterAccountDetailsTestRepository.findByAccountNumber("12345").isPresent();
        assertThat(expected).isTrue();
    }

    @Test
    void notFindByAccountNumber() {
        Meter meterAccountDetails = new Meter();
        meterAccountDetails.setAccountNumber("12345");
        meterAccountDetails.setMeterNumber("54321");
        meterAccountDetailsTestRepository.save(meterAccountDetails);
        boolean expected = meterAccountDetailsTestRepository.findByAccountNumber("1234").isPresent();
        assertThat(expected).isFalse();
    }
}