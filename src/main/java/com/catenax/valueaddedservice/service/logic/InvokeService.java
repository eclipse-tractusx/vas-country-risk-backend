package com.catenax.valueaddedservice.service.logic;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;

@Service
@Slf4j
public class InvokeService {

    RestTemplate defaultRestTemplate = new RestTemplate();
    public Object executeRequest(String url, HttpMethod httpMethod, HttpEntity httpEntity,Object object){
        try{
            return defaultRestTemplate.exchange(url,httpMethod,httpEntity,object.getClass());
        }catch(HttpClientErrorException e){
            log.error("error url {} message {}",url,e.getMessage() );
        }
        return new ArrayList<>();
    }

}
