package com.catenax.valueaddedservice.dto.ShareDTOs;

import com.catenax.valueaddedservice.dto.BusinessPartnerDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ShareBusinessPartnerDTO implements Serializable {

    @Schema(example = "[{\"bpn\":\"bpn01\",\"country\":\"Portugal\"},{\"bpn\":\"bpn02\",\"country\":\"null\"}]")
    private List<BusinessPartnerDTO> businessPartnerDTOS = new ArrayList<>();

    public ShareBusinessPartnerDTO(String params) throws JsonProcessingException {
        this.businessPartnerDTOS = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false).readValue(params, new TypeReference<List<BusinessPartnerDTO>>() {
        });

    }

}
