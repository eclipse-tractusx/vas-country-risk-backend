package com.catenax.valueaddedservice.config;

import com.catenax.valueaddedservice.constants.VasConstants;
import com.catenax.valueaddedservice.dto.AuthPropertiesDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.util.Base64;

@Component
public class ApplicationVariables {

    private String token;

   private AuthPropertiesDTO authPropertiesDTO = new AuthPropertiesDTO();

    public String getToken() {
        return token;
    }

    public AuthPropertiesDTO getAuthPropertiesDTO() {
        return authPropertiesDTO;
    }

    public void setAuthPropertiesDTO(AuthPropertiesDTO authPropertiesDTO) {
        this.authPropertiesDTO = authPropertiesDTO;
    }

    public void setToken(String token) throws JsonProcessingException {
        this.token = token.replace(VasConstants.REMOVE_BEARER_TAG,"");
        decodeJWT();
    }

    public void decodeJWT() throws JsonProcessingException {
        String[] chunks = this.token.split("\\.");
        Base64.Decoder decoder = Base64.getUrlDecoder();
        String header = new String(decoder.decode(chunks[1]));
        this.authPropertiesDTO = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false).readValue(header, AuthPropertiesDTO.class);
        this.authPropertiesDTO.getRoles(this.authPropertiesDTO.getClientResource());
    }



}