package com.asif.meternotifier.util;

import com.asif.meternotifier.dto.Data;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

@Component
public class DataMapperUtil {
    private RequestSenderUtil requestSenderUtil;
    private UrlMakerUtil urlMakerUtil;

    public DataMapperUtil(RequestSenderUtil requestSenderUtil, UrlMakerUtil urlMakerUtil) {
        this.requestSenderUtil = requestSenderUtil;
        this.urlMakerUtil = urlMakerUtil;
    }

    public Data getDataFromMapper(String acNo, String meterNo) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.treeToValue(requestSenderUtil.request(urlMakerUtil.getUrl(acNo, meterNo)), Data.class);
        /*return new Data(acNo, meterNo, 123, "123", "00:00:00");*/
    }
}
