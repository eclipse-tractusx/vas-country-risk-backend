package com.catenax.valueaddedservice.dto.ShareDTOs;

import com.catenax.valueaddedservice.dto.DataSourceDTO;
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
public class ShareDataSourceDTO implements Serializable {

    @Schema(example = "[{\"dataSourceName\":\"teste\",\"yearPublished\":2020},{\"dataSourceName\":\"teste\",\"yearPublished\":2021}]")
    private List<DataSourceDTO> dataSourceDTOS = new ArrayList<>();

    public ShareDataSourceDTO(String params) throws JsonProcessingException {
        this.dataSourceDTOS = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false).readValue(params, new TypeReference<List<DataSourceDTO>>() {
        });

    }

}
