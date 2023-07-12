package com.asif.meternotifier.util;

import com.asif.meternotifier.service.RestService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializer;
import org.springframework.stereotype.Service;


import java.util.Map;

@Service
public class RequestSender {
    private RestService restService;
    public RequestSender(RestService restService){
        this.restService = restService;
    }
    public JsonNode request(String url) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readTree(restService.getPostsPlainJSON(url)).path("data");
    }
}
