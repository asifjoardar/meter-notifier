package com.asif.meternotifier.util;

import com.asif.meternotifier.dto.ApiData;
import com.asif.meternotifier.dto.FormData;
import com.asif.meternotifier.entity.MeterAccountDetails;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class DataMapperUtil {
    private final RestTemplate restTemplate;

    @Value("${data_host}")
    private String dataHost;

    public DataMapperUtil(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    public ApiData getDataFromMapper(String acNo, String meterNo) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        String url = getUrl(acNo, meterNo);
        return mapper.treeToValue(request(url), ApiData.class);
    }

    public static FormData dataMappingByAccountNo(MeterAccountDetails meterAccountDetails) {
        FormData formData = new FormData();
        formData.setEmail(meterAccountDetails.getCustomer().getEmail());
        formData.setAccountNumber(meterAccountDetails.getAccountNumber());
        formData.setMeterNumber(meterAccountDetails.getMeterNumber());
        formData.setBalance(meterAccountDetails.getBalance());
        formData.setNotificationStatus(meterAccountDetails.getNotification().isStatus());
        formData.setMinBalance(meterAccountDetails.getNotification().getMinimumBalance());
        formData.setSendNotificationTo(meterAccountDetails.getNotification().getEmailToSendNotification());
        return formData;
    }

    private JsonNode request(String url) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readTree(restTemplate.getForObject(url, String.class)).path("data");
    }

    private String getUrl(String acNo, String meterNo) {
        return dataHost
                + "/api/tkdes/customer/getBalance?accountNo="
                + acNo
                + "&meterNo="
                + meterNo;
    }
}
