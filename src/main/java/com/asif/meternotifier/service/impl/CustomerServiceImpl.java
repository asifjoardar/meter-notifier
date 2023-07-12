package com.asif.meternotifier.service.impl;

import com.asif.meternotifier.dto.CustomerDto;
import com.asif.meternotifier.entity.ConfirmationToken;
import com.asif.meternotifier.entity.Customer;
import com.asif.meternotifier.entity.MeterAccountDetails;
import com.asif.meternotifier.repository.ConfirmationTokenRepository;
import com.asif.meternotifier.repository.CustomerRepository;
import com.asif.meternotifier.repository.MeterAccountDetailsRepository;
import com.asif.meternotifier.service.CustomerService;
import com.asif.meternotifier.service.EmailService;
import com.asif.meternotifier.util.EmailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomerServiceImpl implements CustomerService {
    private CustomerRepository customerRepository;
    private MeterAccountDetailsRepository meterAccountDetailsRepository;
    private ConfirmationTokenRepository confirmationTokenRepository;

    private EmailSender emailSender;

    public CustomerServiceImpl(CustomerRepository customerRepository,
                               ConfirmationTokenRepository confirmationTokenRepository,
                               MeterAccountDetailsRepository meterAccountDetailsRepository,
                               EmailSender emailSender){
        this.customerRepository = customerRepository;
        this.confirmationTokenRepository = confirmationTokenRepository;
        this.meterAccountDetailsRepository = meterAccountDetailsRepository;
        this.emailSender = emailSender;
    }

    @Override
    public void saveCustomer(CustomerDto customerDto){
        try{
            MeterAccountDetails meterAccountDetails = new MeterAccountDetails();
            if(meterAccountDetailsRepository.findByAccountNumber(customerDto.getAccountNumber()) != null ||
                    meterAccountDetailsRepository.findByMeterNumber(customerDto.getMeterNumber()) != null){
                throw new Exception("Account/Meter number already registered with this mail.");
            }
            meterAccountDetails.setAccountNumber(customerDto.getAccountNumber());
            meterAccountDetails.setMeterNumber(customerDto.getMeterNumber());

            Customer customer = new Customer();
            if(customerRepository.findByEmail(customerDto.getEmail()) != null){
                customer = customerRepository.findByEmail(customerDto.getEmail());
                customer.getMeterAccountDetailsList().add(meterAccountDetails);
                customerRepository.save(customer);
            } else{
                customer.setEmail(customerDto.getEmail());
                customer.getMeterAccountDetailsList().add(meterAccountDetails);
                customerRepository.save(customer);
                // token generate
                ConfirmationToken confirmationToken = new ConfirmationToken(customer);
                confirmationTokenRepository.save(confirmationToken);
                // email sender
                emailSender.send(customerDto.getEmail(), "Complete Registration!", "To confirm your account, please click here : "
                        +"http://localhost:8080/confirm-account?token="+confirmationToken.getConfirmationToken());
            }
            meterAccountDetails.setCustomer(customer);
            meterAccountDetailsRepository.save(meterAccountDetails);

        } catch (Exception e){
            System.out.println(e);
        }
    }

    @Override
    public Customer findCustomerByEmail(String email){
        return customerRepository.findByEmail(email);
    }

    @Override
    public Optional<Customer> findCustomerById(Long id){
        return customerRepository.findById(id);
    }

    @Override
    public boolean confirmEmail(String confirmationToken) {
        ConfirmationToken token = confirmationTokenRepository.findByConfirmationToken(confirmationToken);

        if(token != null)
        {
            Customer customer = customerRepository.findByEmail(token.getCustomer().getEmail());
            customer.setEnabled(true);
            customerRepository.save(customer);
            return true;
        }
        return false;
    }
}
