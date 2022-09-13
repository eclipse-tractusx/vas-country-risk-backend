package com.catenax.valueaddedservice.config;

import com.catenax.valueaddedservice.constants.VasConstants;
import org.springframework.stereotype.Component;

@Component
public class ApplicationVariables {

    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token.replace(VasConstants.REMOVE_BEARER_TAG,"");
    }
}