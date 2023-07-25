package com.asif.meternotifier.repository;

import com.asif.meternotifier.entity.Customer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
class CustomerRepositoryTest {

    @Autowired
    private CustomerRepository customerTestRepository;

    @AfterEach
    void tearDown() {
        customerTestRepository.deleteAll();
    }

    @Test
    void customerShouldFindByEmail() {
        Customer customer = new Customer();
        customer.setEmail("asif@email.com");
        customerTestRepository.save(customer);
        boolean expected = customerTestRepository.findByEmail("asif@email.com").isPresent();
        assertThat(expected).isTrue();
    }

    @Test
    void itShouldCheckIfCustomerEmailDoesNotExist() {
        boolean expected = customerTestRepository.findByEmail("asif@email.com").isPresent();
        assertThat(expected).isFalse();
    }
}