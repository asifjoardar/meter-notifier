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
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomerServiceImpl implements CustomerService {
    private CustomerRepository customerRepository;
    private MeterAccountDetailsRepository meterAccountDetailsRepository;
    private ConfirmationTokenRepository confirmationTokenRepository;
    private EmailService emailService;

    public CustomerServiceImpl(CustomerRepository customerRepository,
                               ConfirmationTokenRepository confirmationTokenRepository,
                               MeterAccountDetailsRepository meterAccountDetailsRepository, EmailService emailService){
        this.customerRepository = customerRepository;
        this.confirmationTokenRepository = confirmationTokenRepository;
        this.meterAccountDetailsRepository = meterAccountDetailsRepository;
        this.emailService = emailService;
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
                SimpleMailMessage mailMessage = new SimpleMailMessage();
                mailMessage.setTo(customerDto.getEmail());
                mailMessage.setSubject("Complete Registration!");
                mailMessage.setText("To confirm your account, please click here : "
                        +"http://localhost:8080/confirm-account?token="+confirmationToken.getConfirmationToken());
                //emailService.sendEmail(mailMessage);

                System.out.println("Confirmation Token: " + confirmationToken.getConfirmationToken());
            }

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
