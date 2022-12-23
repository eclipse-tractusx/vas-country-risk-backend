package com.catenax.valueaddedservice.service.logic;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;

@Service
@Slf4j
public class InvokeService {

    @Autowired
    RestTemplate restTemplate;
    public Object executeRequest(String url, HttpMethod httpMethod, HttpEntity httpEntity,Object object){
        try{
            return restTemplate.exchange(url,httpMethod,httpEntity,object.getClass());
        }catch(HttpClientErrorException e){
            log.error("error url {} message {}",url,e.getMessage() );
        }
        return new ArrayList<>();
    }

}
