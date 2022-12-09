package com.catenax.valueaddedservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * A DTO for the {@link com.catenax.valueaddedservice.domain.CompanyUser} entity.
 */
@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class AuthPropertiesDTO implements Serializable {


    @Schema(example = "John")
    @NotNull
    @JsonProperty("name")
    private String name = "";

    @Schema(example = "John@email.com")
    @NotNull
    @JsonProperty("email")
    private String email = "";

    @Schema(example = "TestCompany")
    @NotNull
    @JsonProperty("organisation")
    private String companyName = "";

    @Schema(example = "\"CX-CRISK\":{\"roles\":[\"GetUser\"]}")
    @NotNull
    @JsonProperty("resource_access")
    private Object resourceAccess = "";

    @Schema(example = "12XRisk")
    @NotNull
    @JsonProperty("azp")
    private String clientResource = "";

    @Schema(example = "true")
    @NotNull
    @JsonProperty("isAdmin")
    private Boolean isAdmin = false;

    public List<String> getRoles(String clientId){
        LinkedHashMap list =  (LinkedHashMap) resourceAccess;
        LinkedHashMap clientResources = list.get(clientId) != null ? (LinkedHashMap) list.get(clientId):new LinkedHashMap();
        List<String> rolesList = clientResources.get("roles")  != null ? (List<String>) clientResources.get("roles"):new ArrayList<>();
        this.isAdmin = rolesList.contains("Admin");
        return rolesList;
    }

}
