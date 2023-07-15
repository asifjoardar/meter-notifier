package com.asif.meternotifier.util;

import com.asif.meternotifier.dto.Data;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

@Service
public class DataMapper {
    private RequestSender requestSender;
    private UrlMaker urlMaker;
    public DataMapper(RequestSender requestSender, UrlMaker urlMaker){
        this.requestSender = requestSender;
        this.urlMaker = urlMaker;
    }
    public Data getDataFromMapper(String acNo, String meterNo) throws JsonProcessingException {
        /*ObjectMapper mapper = new ObjectMapper();
        return mapper.treeToValue(requestSender.request(urlMaker.getUrl(acNo, meterNo)), Data.class);
        */
        return new Data(acNo, meterNo, 123, "123", "00:00:00");
    }
}
