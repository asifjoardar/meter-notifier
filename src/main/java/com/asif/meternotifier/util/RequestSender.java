package com.asif.meternotifier.util;

import com.asif.meternotifier.service.RestService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class RequestSender {
    private RestService restService;
    public RequestSender(RestService restService){
        this.restService = restService;
    }
    public Map<String, Object> request(String url) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(restService.getPostsPlainJSON(url), Map.class);
    }
}