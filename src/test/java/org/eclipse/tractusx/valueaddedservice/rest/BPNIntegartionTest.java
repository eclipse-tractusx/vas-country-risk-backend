/********************************************************************************
* Copyright (c) 2022,2023 BMW Group AG 
* Copyright (c) 2022,2023 Contributors to the Eclipse Foundation
*
* See the NOTICE file(s) distributed with this work for additional
* information regarding copyright ownership.
*
* This program and the accompanying materials are made available under the
* terms of the Apache License, Version 2.0 which is available at
* https://www.apache.org/licenses/LICENSE-2.0.
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
* WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
* License for the specific language governing permissions and limitations
* under the License.
*
* SPDX-License-Identifier: Apache-2.0
********************************************************************************/
package org.eclipse.tractusx.valueaddedservice.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.tractusx.valueaddedservice.ValueAddedServiceApplication;
import org.eclipse.tractusx.valueaddedservice.config.ApplicationVariables;
import org.eclipse.tractusx.valueaddedservice.domain.Company;
import org.eclipse.tractusx.valueaddedservice.domain.CompanyGates;
import org.eclipse.tractusx.valueaddedservice.domain.CompanyGroup;
import org.eclipse.tractusx.valueaddedservice.domain.CompanyUser;
import org.eclipse.tractusx.valueaddedservice.dto.*;
import org.eclipse.tractusx.valueaddedservice.repository.CompanyGatesRepository;
import org.eclipse.tractusx.valueaddedservice.repository.CompanyGroupRepository;
import org.eclipse.tractusx.valueaddedservice.repository.CompanyRepository;
import org.eclipse.tractusx.valueaddedservice.repository.CompanyUserRepository;
import org.eclipse.tractusx.valueaddedservice.utils.MockUtilsTest;
import org.eclipse.tractusx.valueaddedservice.utils.PostgreSQLContextInitializer;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.web.util.UriTemplate;

import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.Mockito.when;

@Slf4j
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,classes = ValueAddedServiceApplication.class)
@ContextConfiguration(initializers = PostgreSQLContextInitializer.class)
class BPNIntegartionTest {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    CompanyGatesRepository companyGatesRepository;

    @Autowired
    CompanyGroupRepository companyGroupRepository;

    @Autowired
    CompanyRepository companyRepository;

    @Autowired
    CompanyUserRepository companyUserRepository;

    @MockBean
    ApplicationVariables applicationVariables;

    MockUtilsTest mockUtilsTest;

    @BeforeAll
    public void beforeAll() {
        mockUtilsTest = new MockUtilsTest();
        mockUtilsTest.beforeAll();
    }


    @BeforeEach
    public void setUp() throws JsonProcessingException {
        AuthPropertiesDTO authPropertiesDTO = new AuthPropertiesDTO();
        authPropertiesDTO.setCompanyName("TestCompany");
        authPropertiesDTO.setEmail("test@email.com");
        authPropertiesDTO.setName("TestName");

        ObjectMapper mapper = new ObjectMapper();
        String json = "{ \"Cl16-CX-CRisk\": { \"roles\": [ \"User\", \"Company Admin\", \"read_suppliers\", \"read_customers\" ] } }";
        Map<String, Object> map = mapper.readValue(json, new TypeReference<>() {
        });
        authPropertiesDTO.setResourceAccess(map);

        when(applicationVariables.getAuthPropertiesDTO()).thenReturn(authPropertiesDTO);
        when(applicationVariables.getToken()).thenReturn("");
        mockUtilsTest.openPorts();

    }


    @AfterEach
    public void cleanGatesAndRelations(){
        companyUserRepository.deleteAll();
        companyGatesRepository.deleteAll();
        companyRepository.deleteAll();
        companyGroupRepository.deleteAll();

    }


    private Map<String,Object> getMap() throws IOException {
        Map<String,Object> map = new HashMap<>();
        map.put("companyName","TestCompany");
        map.put("name","John");
        map.put("email","John@email.com");

        return map;
    }

    @Test
    void getCountryByAssociatedBPtoUser() throws Exception {
        mockUtilsTest.openPorts();
        Map<String,Object> map = getMap();
        UriTemplate uritemplate= new UriTemplate("/api/dashboard/getBpnCountrys?name={name}&companyName={companyName}&email={email}");
        URI uri = uritemplate.expand(map);
        RequestEntity<Void> request = RequestEntity
                .get(uri).build();
        ResponseEntity<List<CountryDTO>> responseEntity = testRestTemplate.exchange(request, new ParameterizedTypeReference<>() {
        });
        List<CountryDTO> list = responseEntity.getBody();
        assertEquals(HttpStatus.OK,responseEntity.getStatusCode());
        assertNotEquals(0,list.size());
    }

    @Test
    void getCompanyBpns() throws Exception {
        mockUtilsTest.openPorts();
        Map<String,Object> map = getMap();
        UriTemplate uritemplate= new UriTemplate("/api/dashboard/getCompanyBpns?name={name}&companyName={companyName}&email={email}");
        URI uri = uritemplate.expand(map);
        RequestEntity<Void> request = RequestEntity
                .get(uri).build();
        ResponseEntity<List<BusinessPartnerDTO>> responseEntity = testRestTemplate.exchange(request,new ParameterizedTypeReference<>() {
        });
        List<BusinessPartnerDTO> list =responseEntity.getBody();

        assertEquals(HttpStatus.OK,responseEntity.getStatusCode());
        assertNotEquals(0,list.size());
    }

    @Test

    void getCountryFilterByISO2() throws Exception {

        Map<String,Object> map = getMap();
        UriTemplate uritemplate= new UriTemplate("/api/dashboard/getCountryFilterByISO2?name={name}&companyName={companyName}&email={email}");
        URI uri = uritemplate.expand(map);
        RequestEntity<Void> request = RequestEntity
                .get(uri).build();
        ResponseEntity<List<CountryDTO>> responseEntity = testRestTemplate.exchange(request,new ParameterizedTypeReference<>() {});
        List<CountryDTO> list = responseEntity.getBody();

        assertEquals(HttpStatus.OK,responseEntity.getStatusCode());
        assertNotEquals(0,list.size());
    }

    @Test
    void getGatesForUser() throws Exception {

        createGateForUser();
        Map<String,Object> map = getMap();
        UriTemplate uritemplate= new UriTemplate("/api/dashboard/getAllUserBPDMGates?name={name}&companyName={companyName}&email={email}");
        URI uri = uritemplate.expand(map);
        RequestEntity<Void> request = RequestEntity
                .get(uri).build();
        ResponseEntity<List<CompanyGatesDTO>> responseEntity = testRestTemplate.exchange(request,new ParameterizedTypeReference<>() {});
        List<CompanyGatesDTO> list = responseEntity.getBody();

        assertEquals(HttpStatus.OK,responseEntity.getStatusCode());
        assertNotEquals(0,list.size());
    }

    @Test
    void getGatesForNonUser() throws Exception {

        Map<String,Object> map = getMap();
        UriTemplate uritemplate= new UriTemplate("/api/dashboard/getAllUserBPDMGates?name={name}&companyName={companyName}&email={email}");
        URI uri = uritemplate.expand(map);
        RequestEntity<Void> request = RequestEntity
                .get(uri).build();
        ResponseEntity<List<CompanyGatesDTO>> responseEntity = testRestTemplate.exchange(request,new ParameterizedTypeReference<>() {});
        List<CompanyGatesDTO> list = responseEntity.getBody();

        assertEquals(HttpStatus.OK,responseEntity.getStatusCode());
        assertEquals(0,list.size());
    }


    public void createGateForUser() throws IOException {
        Map<String,Object> map = getMap();
        Company company = new Company();
        company.setCompanyName(String.valueOf(map.get("companyName")));
        CompanyUser companyUser = new CompanyUser();
        companyUser.setCompanyName(String.valueOf(map.get("companyName")));
        companyUser.setName(String.valueOf(map.get("name")));
        companyUser.setEmail(String.valueOf(map.get("email")));
        CompanyGroup companyGroup = new CompanyGroup();
        companyGroup.setCompanyGroup("TestGroup");
        companyGroup = companyGroupRepository.save(companyGroup);
        company.setCompanyGroup(companyGroup);
        company = companyRepository.save(company);
        CompanyGates companyGates = new CompanyGates();
        companyGates.setGateName("TestGate");
        companyGates.setCompanyGroup(companyGroup);
        companyGatesRepository.save(companyGates);
        companyUser.setCompany(company);
        companyUserRepository.save(companyUser);
    }

    @Test
    void getUserFromCompany() throws Exception {

        CompanyUser companyUser = new CompanyUser();
        companyUser.setName("NewUser");
        companyUser.setCompanyName("TestCompany");
        companyUser.setEmail("NewUser@email.com");

        companyUserRepository.save(companyUser);

        Map<String,Object> map = getMap();
        UriTemplate uritemplate= new UriTemplate("/api/dashboard/getUserFromCompany?name={name}&companyName={companyName}&email={email}");
        URI uri = uritemplate.expand(map);
        RequestEntity<Void> request = RequestEntity
                .get(uri).build();
        ResponseEntity<List<CompanyUserDTO>> responseEntity = testRestTemplate.exchange(request,new ParameterizedTypeReference<>() {});
        List<CompanyUserDTO> list = responseEntity.getBody();

        assertEquals(HttpStatus.OK,responseEntity.getStatusCode());
        assertNotEquals(0,list.size());
    }


}


