package com.asif.meternotifier.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class UrlMakerUtil {
    @Value("${data_host}")
    private String dataHost;

    public String getUrl(String acNo, String meterNo) {
        return dataHost
                + "/api/tkdes/customer/getBalance?accountNo="
                + acNo
                + "&meterNo="
                + meterNo;
    }
}
