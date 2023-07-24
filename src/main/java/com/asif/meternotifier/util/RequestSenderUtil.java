package com.asif.meternotifier.util;

import com.asif.meternotifier.service.RestService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

@Component
public class RequestSenderUtil {
    private final RestService restService;

    public RequestSenderUtil(RestService restService) {
        this.restService = restService;
    }

    public JsonNode request(String url) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readTree(restService.getPostsPlainJSON(url)).path("data");
    }
}
