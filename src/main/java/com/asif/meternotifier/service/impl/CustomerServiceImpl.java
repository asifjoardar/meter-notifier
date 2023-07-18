package com.asif.meternotifier.service.impl;

import com.asif.meternotifier.entity.ConfirmationToken;
import com.asif.meternotifier.entity.Customer;
import com.asif.meternotifier.entity.MeterAccountDetails;
import com.asif.meternotifier.entity.Notification;
import com.asif.meternotifier.repository.ConfirmationTokenRepository;
import com.asif.meternotifier.repository.CustomerRepository;
import com.asif.meternotifier.repository.MeterAccountDetailsRepository;
import com.asif.meternotifier.repository.NotificationRepository;
import com.asif.meternotifier.service.CustomerService;
import com.asif.meternotifier.util.EmailSender;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomerServiceImpl implements CustomerService {
    private CustomerRepository customerRepository;
    private MeterAccountDetailsRepository meterAccountDetailsRepository;
    private ConfirmationTokenRepository confirmationTokenRepository;
    private EmailSender emailSender;
    private NotificationRepository notificationRepository;

    public CustomerServiceImpl(CustomerRepository customerRepository,
                               ConfirmationTokenRepository confirmationTokenRepository,
                               MeterAccountDetailsRepository meterAccountDetailsRepository,
                               EmailSender emailSender,
                               NotificationRepository notificationRepository){
        this.customerRepository = customerRepository;
        this.confirmationTokenRepository = confirmationTokenRepository;
        this.meterAccountDetailsRepository = meterAccountDetailsRepository;
        this.emailSender = emailSender;
        this.notificationRepository = notificationRepository;
    }

    @Override
    public void saveCustomer(Customer customer, MeterAccountDetails meterAccountDetails){
        try{
            if(meterAccountDetailsRepository.findByAccountNumber(meterAccountDetails.getAccountNumber()) != null &&
                    meterAccountDetailsRepository.findByMeterNumber(meterAccountDetails.getMeterNumber()) != null){
                throw new Exception("Account and Meter number already registered.");
            }
            if(customerRepository.findByEmail(customer.getEmail()) == null){
                customerRepository.save(customer);
                // token generate
                ConfirmationToken confirmationToken = new ConfirmationToken(customer);
                confirmationTokenRepository.save(confirmationToken);
                // email sender
                emailSender.send(customer.getEmail(), "Complete Registration!", "To confirm your account, please click here : "
                        +"http://localhost:8080/confirm-account?token="+confirmationToken.getConfirmationToken());
            }

            Notification notification = new Notification();
            notification.setEmailToSendNotification(customer.getEmail());
            notificationRepository.save(notification);

            meterAccountDetailsRepository.save(meterAccountDetails);
            customer.getMeterAccountDetailsList().add(meterAccountDetails);
            customerRepository.save(customer);

            meterAccountDetails.setCustomer(customer);
            meterAccountDetails.setBalance(1530.50);
            meterAccountDetails.setNotification(notification);
            meterAccountDetailsRepository.save(meterAccountDetails);

        } catch (Exception e){
            System.out.println(e);
        }
    }
    @Override
    public void updateCustomer(MeterAccountDetails meterAccountDetails){
        meterAccountDetailsRepository.save(meterAccountDetails);
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
