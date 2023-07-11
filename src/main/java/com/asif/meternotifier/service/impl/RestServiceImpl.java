package com.asif.meternotifier.service.impl;

import com.asif.meternotifier.service.RestService;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class RestServiceImpl implements RestService {
    private RestTemplate restTemplate;

    public RestServiceImpl(RestTemplateBuilder restTemplateBuilder){
        this.restTemplate = restTemplateBuilder.build();
    }
    public String getPostsPlainJSON(String url){
        return restTemplate.getForObject(url, String.class);
    }
}
