package com.asif.meternotifier.util;

import org.springframework.stereotype.Service;

@Service
public class UrlMaker {
    public String getUrl(String acNo, String meterNo){
        return "http://prepaid.desco.org.bd/api/tkdes/customer/getBalance?accountNo="+ acNo + "&meterNo="+ meterNo;
    }
}