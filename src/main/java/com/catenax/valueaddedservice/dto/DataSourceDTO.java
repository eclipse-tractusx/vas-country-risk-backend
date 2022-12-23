package com.catenax.valueaddedservice.dto;

import com.catenax.valueaddedservice.domain.enumeration.Type;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.io.Serializable;


/**
 * A DTO for the {@link com.catenax.valueaddedservice.domain.DataSource} entity.
 */
@Setter
@Getter
@ToString
@NoArgsConstructor
@JsonIgnoreProperties("companyUser")
public class DataSourceDTO implements Serializable {

    private Long id;

    @Schema(example = "Fake Rating")
    @NotNull
    private String dataSourceName;

    @Schema(example = "Custom")
    @NotNull
    private Type type;

    @Schema(example = "2021")
    @NotNull
    private Integer yearPublished;

    @Schema(example = "Test Company Rating")
    private String fileName;

    private CompanyUserDTO companyUser;


    public DataSourceDTO(String json) {

        try {
            DataSourceDTO dataSourceDTO = new ObjectMapper()
                    .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false).readValue(json, DataSourceDTO.class);
            this.dataSourceName = dataSourceDTO.getDataSourceName();
            this.yearPublished = dataSourceDTO.getYearPublished();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

    }
}
