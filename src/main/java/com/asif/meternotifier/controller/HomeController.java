package com.asif.meternotifier.controller;

import com.asif.meternotifier.dto.CustomerDto;
import com.asif.meternotifier.dto.LoginDto;
import com.asif.meternotifier.entity.Customer;
import com.asif.meternotifier.entity.MeterAccountDetails;
import com.asif.meternotifier.service.CustomerService;
import com.asif.meternotifier.service.RestService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.io.*;

@Controller
public class HomeController {

    private CustomerService customerService;

    private RestService restService;

    public HomeController(CustomerService customerService, RestService restService){
        this.customerService = customerService;
        this.restService = restService;
    }

    @GetMapping("/")
    public String showLoginForm(Model model){
        model.addAttribute("login", new LoginDto());
        return "login";
    }
    @PostMapping("/")
    public String login(@ModelAttribute(value = "login") LoginDto loginDto){
        //if(customerService.findCustomerByEmail(loginDto.getEmail()) != null && customerService.findCustomerByEmail(loginDto.getEmail()).isEnabled())
        if(customerService.findCustomerByEmail(loginDto.getEmail()) != null){
            return "redirect:/customer-info/"+customerService.findCustomerByEmail(loginDto.getEmail()).getId();
        } else {
            return "redirect:/";
        }
    }
    @GetMapping("/registration")
    public String showRegistrationForm(Model model){
        model.addAttribute("customer", new CustomerDto());
        return "registration";
    }
    @PostMapping("/registration")
    public String registration(@ModelAttribute(value = "customer") CustomerDto customerDto,
                               BindingResult result,
                               Model model){
        System.out.println(customerDto);
        customerService.saveCustomer(customerDto);
        return "redirect:/";
    }
    @RequestMapping(value="/confirm-account", method= {RequestMethod.GET, RequestMethod.POST})
    public String confirmUserAccount(@RequestParam("token")String confirmationToken) {
        try {
            if(customerService.confirmEmail(confirmationToken)){
                return "login";
            }
            else {
                throw new Exception("Token is not valid");
            }

        } catch (Exception e){
            System.out.println(e);
            return "login";
        }
    }

    @GetMapping("/customer-info/{id}")
    public String customerInfo(@PathVariable(value = "id") Long id, Model model){
        Optional<Customer> customer = customerService.findCustomerById(id);
        model.addAttribute("accountDetails", customer.get().getMeterAccountDetailsList());
        System.out.println(customer.get().getMeterAccountDetailsList());
        return "customer-info";
    }

    @GetMapping("/customer-account/{accountNumber}")
    public String customerInfoDetails(@PathVariable("accountNumber") String accountNumber) {
        // sending get request to the desco api
        /*String url = "http://prepaid.desco.org.bd/api/tkdes/customer/getBalance?accountNo="+accountDetails.get("accountNumber")+"&meterNo="+accountDetails.get("meterNumber");
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> response = mapper.readValue(restService.getPostsPlainJSON(url), Map.class);
        model.addAttribute("customer", response.get("data"));*/
        /*Map<String, String>data = new HashMap<>();
        data.put("accountNo", "12042012");
        data.put("meterNo", "663110109424");
        data.put("balance", "1369.58");
        data.put("currentMonthConsumption", "1249.35");
        data.put("readingTime", "2023-07-11 00:00:00");
        model.addAttribute("customer", data);*/
        return "customer-account-details";
    }
}
