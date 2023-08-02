package com.asif.meternotifier.util;

import com.asif.meternotifier.dto.ApiDataDto;
import com.asif.meternotifier.exception.NotFoundException;
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

    public ApiDataDto getCustomerDataFromApi(String acNo, String meterNo) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        String url = getUrl(acNo, meterNo);
        ApiDataDto apiData = mapper.treeToValue(request(url), ApiDataDto.class);
        if (apiData == null) {
            throw new NotFoundException("The Account No. does not exist");
        }
        return apiData;
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
