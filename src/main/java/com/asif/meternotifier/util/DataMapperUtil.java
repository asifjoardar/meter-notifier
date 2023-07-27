package com.asif.meternotifier.util;

import com.asif.meternotifier.dto.Data;
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

    public Data getDataFromMapper(String acNo, String meterNo) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        String url = getUrl(acNo, meterNo);
        return mapper.treeToValue(request(url), Data.class);
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
