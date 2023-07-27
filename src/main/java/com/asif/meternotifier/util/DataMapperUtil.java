package com.asif.meternotifier.util;

import com.asif.meternotifier.dto.Data;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class DataMapperUtil {
    private final UrlMakerUtil urlMakerUtil;
    private final RestTemplate restTemplate;

    public DataMapperUtil(UrlMakerUtil urlMakerUtil, RestTemplateBuilder restTemplateBuilder) {
        this.urlMakerUtil = urlMakerUtil;
        this.restTemplate = restTemplateBuilder.build();
    }

    public Data getDataFromMapper(String acNo, String meterNo) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        String url = urlMakerUtil.getUrl(acNo, meterNo);
        return mapper.treeToValue(request(url), Data.class);
    }

    private JsonNode request(String url) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readTree(restTemplate.getForObject(url, String.class)).path("data");
    }
}
